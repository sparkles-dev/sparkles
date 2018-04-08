package sparkles.paypal.payments;

import com.squareup.moshi.Json;
import lombok.experimental.Accessors;
import lombok.Data;

@Accessors(fluent  = true)
@Data
public class RedirectUrls {

  /**
   * The URL where the payer is redirected after he or she approves the payment.
   *
   * Required for PayPal account payments.
   */
  @Json(name = "return_url")
  private String returnUrl;

  /**
   * The URL where the payer is redirected after he or she cancels the payment.
   *
   * Required for PayPal account payments.
   */
  @Json(name = "cancel_url")
  private String cancelUrl;

}
