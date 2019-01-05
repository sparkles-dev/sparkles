package sparkles.support.json.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.util.UUID;

import sparkles.support.common.ToStringBuilder;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonResourcesModuleTest {

  private final ObjectMapper om = new ObjectMapper()
    .registerModule(new JsonResourcesModule())
    .setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

  @Test
  public void embedded_shouldSerialize() throws Exception {
    Foo foo = new Foo();
    foo.related = new Bar("hello world!");

    String serialized = om.writeValueAsString(foo);
    assertThat(serialized).isEqualTo("{\"_embedded\":{\"related\":{\"name\":\"hello world!\"}}}");
  }

  @Test
  public void embedded_shouldDeserialize() throws Exception {
    Foo deserialized = om.readValue("{\"_embedded\":{\"related\":{\"name\":\"hello world!\"}}}", Foo.class);
    assertThat(deserialized).isNotNull();
    assertThat(deserialized.related).isNotNull();
    assertThat(deserialized.related.name).isEqualTo("hello world!");
  }

  @Test
  public void shouldSerializeProperties() throws Exception {
    Foo foo = new Foo();
    foo.id = UUID.fromString("f6eb1bd7-7611-49b1-ba15-5d1fa59a30a3");

    String serialized = om.writeValueAsString(foo);
    assertThat(serialized).isEqualTo("{\"id\":\"f6eb1bd7-7611-49b1-ba15-5d1fa59a30a3\"}");
  }

  @Test
  public void itShouldConsiderOnlyResourceAnnotatedClasses() throws Exception {
    NoResource noResource = new NoResource();
    noResource.related = new Bar("foo");
    noResource.links = new LinkCollection()
      .add(new Link().rel("foo").href("/foo/bar"));

    String serialized = om.writeValueAsString(noResource);
    assertThat(serialized).doesNotContain("_embedded");
    assertThat(serialized).doesNotContain("_links");
  }

  @Resource
  private static class Foo {

    @Links
    public LinkCollection links;

    @Embedded
    public Bar related;

    public UUID id;

    @Override
    public String toString() {
      return new ToStringBuilder(Foo.class)
        .append("id", id)
        .append("related", related)
        .toString();
    }
  }

  @Resource
  private static class Bar {

    public String name;

    public Bar() {}

    public Bar(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return "Bar[name=" + name + "]";
    }
  }

  private static class NoResource {

    @Links
    public LinkCollection links;

    @Embedded
    public Bar related;
  }

}
