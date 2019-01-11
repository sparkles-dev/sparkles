package sparkles.support.json.resources.internal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LinkMixin {

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
}
