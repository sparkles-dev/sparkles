package sparkles.paypal.payments;

import com.squareup.moshi.Json;
import lombok.experimental.Accessors;
import lombok.Data;

@Accessors(fluent  = true)
@Data
public class LinkDescription {

  /**
   * The complete target URL. To make the related call, combine the method with this URI Template-formatted link. For pre-processing, include the $, (, and ) characters.
   *
   * The href is the key HATEOAS component that links a completed call with a subsequent call.
   *
   * Required.
   */
  @Json(name = "href")
  private String href;

  /**
   * The link relation type, which serves as an ID for a link that unambiguously describes the semantics of the link. See Link Relations.
   * Required.
   */
  @Json(name = "rel")
  private String rel;

  /**
   * The HTTP method required to make the related call.
   */
  @Json(name = "method")
  private Method method;

  /** Possible values: GET, POST, PUT, DELETE, HEAD, CONNECT, OPTIONS, PATCH. */
  public static enum Method {
    GET,
    POST,
    PUT,
    DELETE,
    HEAD,
    CONNECT,
    OPTIONS,
    PATCH;
  }
}
