package sparkles.paypal.payments;

import com.squareup.moshi.Json;
import lombok.experimental.Accessors;
import lombok.Data;

@Accessors(fluent  = true)
@Data
public class Amount {

  /** The three-character ISO-4217 currency code. PayPal does not support all currencies. */
  @Json(name = "currency")
  private String currency;

  /**
   * The total amount charged to the payee by the payer.
   *
   * For refunds, represents the amount that the payee refunds to the original payer.
   * Maximum length is 10 characters, which includes:
   *  - Seven digits before the decimal point.
   *  - The decimal point.
   *  - Two digits after the decimal point.
   */
  @Json(name = "total")
  private String total;

}
