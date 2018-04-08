package sparkles.paypal.payments;

import com.squareup.moshi.Json;
import lombok.experimental.Accessors;
import lombok.Data;

@Accessors(fluent  = true)
@Data
public class Payer {

  /** The payment method. Value is paypal for a PayPal Wallet payment. */
  @Json(name = "payment_method")
  private PaymentMethod paymentMethod;

}
