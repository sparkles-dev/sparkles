package sparkles.support.json.hal;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonInclude;

import sparkles.support.json.hal.annotation.Links;
import sparkles.support.json.hal.annotation.Resource;

@Resource
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceSupport<Domain> {

  private Link self;
  private Domain domain;

  @JsonUnwrapped
  public Domain getDomain() {
    return domain;
  }

  public ResourceSupport<Domain> withDomain(Domain domain) {
    this.domain = domain;

    return this;
  }

  @Links
  public Link getSelf() {
    return self;
  }

  public ResourceSupport<Domain> withSelf(Link link) {
    this.self = link;

    return this;
  }

}
