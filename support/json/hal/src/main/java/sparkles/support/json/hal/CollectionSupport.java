package sparkles.support.json.hal;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;

import sparkles.support.json.hal.annotation.Embedded;
import sparkles.support.json.hal.annotation.Links;
import sparkles.support.json.hal.annotation.Resource;

@Resource
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollectionSupport<Domain> {

  private Link self;
  private Collection<ResourceSupport<Domain>> content;

  @Embedded
  public Collection<ResourceSupport<Domain>> getContent() {
    return content;
  }

  public CollectionSupport<Domain> withContent(Collection<ResourceSupport<Domain>> content) {
    this.content = content;

    return this;
  }

  @Links
  public Link getSelf() {
    return self;
  }

  public CollectionSupport<Domain> withSelf(Link link) {
    this.self = link;

    return this;
  }

}
