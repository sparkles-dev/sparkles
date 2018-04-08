package sparkles.paypal.payments;

import com.squareup.moshi.Json;

/**
 * The payment intent.
 *
 * Possible values: sale, authorize, order.
 */
public enum Intent {

  /** Makes an immediate payment. */
  @Json(name = "sale") SALE,

  /** Authorizes a payment for capture later. */
  @Json(name = "authorize") AUTHORIZE,

  /** Creates an order. */
  @Json(name = "order") ORDER;
}
