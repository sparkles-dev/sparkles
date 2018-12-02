package sparkles.support.json.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Link {

  @JsonProperty
  private String href;

  @JsonProperty
  private String rel;

  @JsonProperty
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
    if (this.templated) {
      // TODO: implement link expansion with uri templates

      return this.href;
    } else {
      return this.href;
    }
  }

}
