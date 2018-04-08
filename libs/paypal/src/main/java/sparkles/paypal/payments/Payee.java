package sparkles.paypal.payments;

import com.squareup.moshi.Json;

public class Payee {

  /**
   * The email address associated with the payee's PayPal account.
   *
   * For an intent of authorize or order, the email address must be associated with a confirmed PayPal business account.
   *
   * For an intent of sale, the email can either:
   *  - Be associated with a confirmed PayPal personal or business account.
   *  - Not be associated with a PayPal account.
   */
  @Json(name = "email")
  private String email;

  /**
   * The PayPal account ID for the payee.
   */
  @Json(name = "merchant_id")
  private String merchantId;

  /**
   * The display-only metadata for the payee.
   */
  @Json(name = "payee_display_metadata")
  private String payeeDisplayMetadata;

}
