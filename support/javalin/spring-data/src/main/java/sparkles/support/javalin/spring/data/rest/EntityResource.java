package sparkles.support.javalin.spring.data.rest;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import sparkles.support.json.resources.Link;
import sparkles.support.json.resources.Links;
import sparkles.support.json.resources.LinkCollection;
import sparkles.support.json.resources.Resource;

/**
 * JSON resource representation for an entity.
 *
 * @param <Entity>
 */
@Resource
public class EntityResource<Entity> {

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
