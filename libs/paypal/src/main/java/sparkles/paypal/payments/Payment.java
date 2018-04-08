package sparkles.paypal.payments;

import java.util.List;
import com.squareup.moshi.Json;
import lombok.experimental.Accessors;
import lombok.Data;

@Accessors(fluent  = true)
@Data
public class Payment {

  @Json(name = "id")
  private String id;

  /**
   * Set to one of the following:
   *  - `sale`. Makes an immediate Express Checkout payment.
   *  - `authorize`. Places the funds on hold for a later payment.
   *  - `order`. Indicates that the buyer has consented to a future purchase. Funds are authorized and captured at a later time without placing the funds on hold.
   *
   * Authorizations are guaranteed for up to three days, though you can attempt to capture an authorization for up to 29 days. After the three-day honor period authorization expires, you can reauthorize the payment.
   */
  @Json(name = "intent")
  private String intent;

  @Json(name = "experience_profile_id")
  private String experienceProfileId;

  @Json(name = "redirect_urls")
  private RedirectUrls redirectUrls;

  @Json(name = "payer")
  private Payer payer;

  @Json(name = "transactions")
  private List<Transaction> transactions;

}
