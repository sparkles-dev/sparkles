package sparkles.paypal.payments;

import com.squareup.moshi.Json;

/**
 * The payment method.
 *
 * Value is paypal for a PayPal Wallet payment.
 */
public enum PaymentMethod {

  @Json(name = "paypal") PAYPAL;
}
