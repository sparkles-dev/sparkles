package sparkles.support.json.resources;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class JacksonResourcesModuleTests {

  private static class MyDomain {
    public String foo = "bar";
    public Double bar = Double.valueOf("23.8");
  }

  @Resource
  private static class EntityResource<Entity> {

    private final LinkCollection links = new LinkCollection();

    @JsonUnwrapped
    public Entity entity;

    @Links
    public LinkCollection getLinks() {
      return links;
    }

    public EntityResource<Entity> withEntity(Entity entity) {
      this.entity = entity;

      return this;
    }

    public EntityResource<Entity> withSelfRel(String href) {
      links.add(new Link().rel("self").href(href));

      return this;
    }

  }

  public static class EntityCollectionResource<Entity> extends EntityResource<EntityCollectionResource.Data> {

    private List<EntityResource<Entity>> content = new ArrayList<>();

    @Embedded("content")
    public List<EntityResource<Entity>> getContent() {
      return content;
    }

    public EntityCollectionResource<Entity> withContent(List<EntityResource<Entity>> content) {
      this.content.clear();
      this.content.addAll(content);
      this.entity = new Data();
      this.entity.count = content.size();

      return this;
    }

    public static <Entity> EntityCollectionResource<Entity> from(List<EntityResource<Entity>> entities) {
      return new EntityCollectionResource<Entity>().withContent(entities);
    }

    public static class Data {
      public long count;
      public long total;
    }
  }

  private final ObjectMapper om = new ObjectMapper()
    .registerModule(new JacksonResourcesModule());

  @Test
  public void itShouldCreate() {
    assertThat(om).isNotNull();
  }

  @Test
  @Ignore
  public void entityResource_itShouldSerialize() throws JsonProcessingException {
    final EntityResource<MyDomain> value = new EntityResource<MyDomain>()
      .withEntity(new MyDomain())
      .withSelfRel("/foo/bar/123");

    final String jsonString = om.writeValueAsString(value);
    assertThat(jsonString).isNull();
  }

  @Test
  @Ignore
  public void entityCollectionResource_itShouldSerialize() throws JsonProcessingException {
    final EntityCollectionResource<MyDomain> value = EntityCollectionResource.from(Arrays.asList(
      new EntityResource<MyDomain>()
        .withEntity(new MyDomain())
        .withSelfRel("/foo/bar/123"),
      new EntityResource<MyDomain>()
        .withEntity(new MyDomain())
        .withSelfRel("/foo/bar/789")
    ));

    final String jsonString = om.writeValueAsString(value);
    assertThat(jsonString).isNull();
  }

  @Test
  public void itShouldDeserialize() throws IOException {
    final EntityCollectionResource<MyDomain> value = EntityCollectionResource.from(Arrays.asList(
      new EntityResource<MyDomain>()
        .withEntity(new MyDomain())
        .withSelfRel("/foo/bar/123"),
      new EntityResource<MyDomain>()
        .withEntity(new MyDomain())
        .withSelfRel("/foo/bar/789")
    ));
    value.withSelfRel("/collection");

    final String jsonString = om.writeValueAsString(value);
    assertThat(jsonString).isNotEmpty();

    final EntityCollectionResource<MyDomain> foo = (EntityCollectionResource<MyDomain>) om.readValue(jsonString, EntityCollectionResource.class);
    assertThat(foo).isNotNull();
    assertThat(foo.entity.count).isEqualTo(2);
    /*
    assertThat(foo.getLinks()).isNotNull();
    assertThat(foo.getLinks().links()).hasSize(1);
    */
    assertThat(foo.getContent()).hasSize(2);
  }

}
