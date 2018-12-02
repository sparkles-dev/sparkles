package sparkles.support.javalin.spring.data.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Link {

  @JsonProperty
  private String rel;

  @JsonProperty
  private String href;

}
