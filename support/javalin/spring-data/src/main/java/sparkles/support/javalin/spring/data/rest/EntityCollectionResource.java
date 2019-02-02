package sparkles.support.javalin.spring.data.rest;

import java.util.ArrayList;
import java.util.List;

import sparkles.support.json.resources.Embedded;

/**
 * JSON resource representation for a collection of entities.
 *
 * @param <Entity>
 */
public class EntityCollectionResource<Entity> extends EntityResource<EntityCollectionResource.Metadata> {

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
