package sparkles.paypal.payments;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * PayPal Payments API.
 *
 * Base URL (Sandbox): `https://api.sandbox.paypal.com/v1/payments/`
 *
 * @link https://developer.paypal.com/docs/integration/direct/express-checkout/integration-jsv4/server-side-REST-integration/
 */
public interface PaymentsApi {

  /**
   * Creates a payment.
   *
   * To create an Express Checkout payment, set the payment intent to sale and include a list of items in the transaction object.
   * The items enable buyers to see the transaction total when they check out at PayPal.
   * If you omit items from the request, the PayPal windows do not display the transaction total.
   *
   * @link https://developer.paypal.com/docs/integration/direct/express-checkout/integration-jsv4/advanced-payments-api/create-express-checkout-payments/
   */
  @Headers("Content-Type: application/json")
  @POST("payment")
  Call<Payment> createPayment(
    @Body Payment payment
  );

  /**
   * When you execute an Express Checkout payment, the transaction completes and moves money from the buyer's PayPal account into your merchant PayPal account.
   *
   * To execute a payment, include the payment ID in the URI and include a payment object in the JSON body.
   * The payment object includes the PayPal-generated payer ID and any final line item details.
   * These item details include any additional shipping costs and taxes.
   *
   * @link https://developer.paypal.com/docs/integration/direct/express-checkout/integration-jsv4/advanced-payments-api/execute-payments/
   */
  @Headers("Content-Type: application/json")
  @POST("payment/{paymentId}/execute")
  Call<Payment> executePaymentById(
    @Path("paymentId") String paymentId,
    @Body PaymentExecution payment
  );

  @Headers("Content-Type: application/json")
  @POST
  Call<Payment> executePayment(
    @Url String url,
    @Body PaymentExecution payment
  );

}
