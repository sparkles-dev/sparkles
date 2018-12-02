package sparkles.support.json.resources;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonResourceModuleTests {

  private static class MyDomain {
    public String foo = "bar";
    public Double bar = Double.valueOf("23.8");
  }

  @Resource
  private static class EntityResource<Entity> {

    private final Links links = new Links();

    @JsonUnwrapped
    public Entity entity;

    @Linked
    public Links getLinks() {
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

  public static class EntityCollectionResource<Entity> extends EntityResource<EntityCollectionResource.Metadata> {

    private List<EntityResource<Entity>> content = new ArrayList<>();

    @Embedded("content")
    public List<EntityResource<Entity>> getContent() {
      return content;
    }

    public void setContent(List<EntityResource<Entity>> content) {
      this.content = content;
    }

    public static <Entity> EntityCollectionResource<Entity> from(List<EntityResource<Entity>> entities) {
      EntityCollectionResource<Entity> resource = new EntityCollectionResource<>();
      resource.entity = new Metadata();
      resource.entity.count = entities.size();
      resource.content.addAll(entities);

      return resource;
    }

    public static class Metadata {
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
  public void entityResource_itShouldSerialize() throws JsonProcessingException {
    final EntityResource<MyDomain> value = new EntityResource<MyDomain>()
      .withEntity(new MyDomain())
      .withSelfRel("/foo/bar/123");

    final String jsonString = om.writeValueAsString(value);
    assertThat(jsonString).isNull();
  }

  @Test
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
  public void itShouldDeserialize() {

  }


}
