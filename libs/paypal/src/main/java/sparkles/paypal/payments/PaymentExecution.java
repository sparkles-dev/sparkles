package sparkles.paypal.payments;

import com.squareup.moshi.Json;
import lombok.experimental.Accessors;
import lombok.Data;

@Accessors(fluent = true)
@Data
public class PaymentExecution {

  /** The ID of the payer that PayPal passes in the return_url. */
  @Json(name = "payer_id")
  private String payerId;

}
