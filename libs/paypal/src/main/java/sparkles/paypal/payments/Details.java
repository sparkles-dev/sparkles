package sparkles.paypal.payments;

import com.squareup.moshi.Json;

public class Details {

  /**
   * The subtotal amount for the items.
   *
   * If the request includes line items, this property is required.
   * Maximum length is 10 characters, which includes:
   *  - Seven digits before the decimal point.
   *  - The decimal point.
   *  - Two digits after the decimal point.
   */
  @Json(name = "subtotal")
  public String subtotal;

  /**
   * The shipping fee.
   *
   * Maximum length is 10 characters, which includes:
   *  - Seven digits before the decimal point.
   *  - The decimal point.
   *  - Two digits after the decimal point.
   */
  @Json(name = "shipping")
  public String shipping;

  /**
   * The tax.
   *
   * Maximum length is 10 characters, which includes:
   *  - Seven digits before the decimal point.
   *  - The decimal point.
   *  - Two digits after the decimal point.
   */
  @Json(name = "tax")
  public String tax;

  /**
   * The handling fee.
   *
   * Maximum length is 10 characters, which includes:
   *  - Seven digits before the decimal point.
   *  - The decimal point.
   *  - Two digits after the decimal point.
   *
   * Supported for the PayPal payment method only.
   */
  @Json(name = "handling_fee")
  public String handlingFee;

  /**
   * The shipping fee discount.
   *
   * Maximum length is 10 characters, which includes:
   *  - Seven digits before the decimal point.
   *  - The decimal point.
   *  - Two digits after the decimal point.
   *
   * Supported for the PayPal payment method only.
   */
  @Json(name = "shipping_discount")
  public String shippingDiscount;

  /**
   * The insurance fee.
   *
   * Maximum length is 10 characters, which includes:
   *  - Seven digits before the decimal point.
   *  - The decimal point.
   *  - Two digits after the decimal point.
   *
   * Supported only for the PayPal payment method.
   */
  @Json(name = "insurance")
  public String insurance;

  /**
   * The gift wrap fee.
   *
   * Maximum length is 10 characters, which includes:
   *  - Seven digits before the decimal point.
   *  - The decimal point.
   *  - Two digits after the decimal point.
   */
  @Json(name = "gift_wrap")
  public String giftWrap;

}
