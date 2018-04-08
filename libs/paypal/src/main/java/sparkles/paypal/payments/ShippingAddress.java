package sparkles.paypal.payments;

import com.squareup.moshi.Json;

public class ShippingAddress {

  /**
   * The first line of the address. For example, number, street, and so on.
   */
  @Json(name = "line1")
  public String line1;

  /**
   * The two-character ISO 3166-1 code that identifies the country or region.
   */
  @Json(name = "country_code")
  public String countryCode;

}
