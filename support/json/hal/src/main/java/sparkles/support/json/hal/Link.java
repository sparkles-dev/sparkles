package sparkles.support.json.hal;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.net.URL;

/**
 * Representation of link as defined in HAL - see
 * <a href="http://tools.ietf.org/html/draft-kelly-json-hal-06">JSON Hypertext Application Language</a>.
 *
 * This edition of the HAL Link includes a proposed change to the HAL specifcation to includes a temporal
 * aspect in the link object, here named {@code seen}. Due to the HyperText Cache Pattern it is included in the link.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Link {

  private String href;
  private Boolean templated;
  private String type;
  private URL deprecation;
  private String name;
  private URI profile;
  private String title;
  private String hreflang;

  private Link() {}

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public static class Builder {
    private String href;

    public Builder(Link link) {
      this.href = link.href;
    }

    public Builder() {
    }

    public Link build() {
      Link link = new Link();
      link.href = href;

      return link;
    }

    public Builder slash(String value) {
      href = href.concat("/").concat(value);
      return this;
    }

    public Builder slash(Object value) {
      return slash(value.toString());
    }
  }
}
