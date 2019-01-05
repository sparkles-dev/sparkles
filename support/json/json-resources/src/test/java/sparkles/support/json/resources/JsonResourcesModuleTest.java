package sparkles.support.json.resources;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonResourcesModuleTest {

  private final ObjectMapper om = new ObjectMapper()
    .registerModule(new JsonResourcesModule());

  @Test
  public void foo() throws Exception {
    Foo foo = new Foo();
    foo.related = new Bar("hello world!");

    String serialized = om.writeValueAsString(foo);
    //assertThat(serialized).isNull();

    Foo deserialized = om.readValue(serialized, Foo.class);
    assertThat(deserialized).isNull();
  }

  @Resource
  private static class Foo {

    @Embedded
    public Bar related;

    @Override
    public String toString() {
      return "Foo[related=" + related.toString() + "]";
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

}
