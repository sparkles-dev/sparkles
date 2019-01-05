package sparkles.support.json.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Link {

  @JsonProperty
  private String href;

  @JsonIgnore
  private String rel;

  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  private boolean templated;

  @JsonProperty
  private String type;

  @JsonProperty
  private String name;

  @JsonProperty
  private String lang;

  @JsonProperty
  private String title;

  public String expand(Object... value) {
    // https://github.com/damnhandy/Handy-URI-Templates#basic-usage

    if (this.templated) {
      // TODO: implement link expansion with uri templates

      return this.href;
    } else {
      return this.href;
    }
  }

  public static Link withRel(String rel) {
    return new Link().rel(rel);
  }

  public static Link withSelfRel() {
    return new Link().rel("self");
  }

}
