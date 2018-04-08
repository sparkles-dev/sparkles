package sparkles.paypal.payments;

import com.squareup.moshi.Json;

public class PaymentOptions {

  /**
   * The payment method for this transaction.
   *
   * This field does not apply to the credit card payment method.
   *
   * Default: UNRESTRICTED.
   */
  @Json(name = "allowed_payment_method")
  public AllowedPaymentMethod allowedPaymentMethod;

  /** Possible values: UNRESTRICTED, INSTANT_FUNDING_SOURCE, IMMEDIATE_PAY. */
  public static enum AllowedPaymentMethod {

    /**
     * Merchant does not have a preference on how they want the customer to pay.
     */
    UNRESTRICTED,
    /**
     * Merchant requires that the customer pays with an instant funding source, such as a credit card or PayPal balance.
     * All payments are processed instantly.
     * However, payments that require a manual review are marked as pending.
     * Merchants must handle the pending state as if the payment is not yet complete.
     */
    INSTANT_FUNDING_SOURCE,

    /**
     * Processes all payments immediately.
     * Any payment that requires a manual review is marked failed.
     */
    IMMEDIATE_PAY;
  }
}
