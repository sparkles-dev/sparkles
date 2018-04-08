package sparkles.paypal;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;
import sparkles.paypal.oauth2.OAuth2Api;
import sparkles.paypal.oauth2.TokenGrant;
import sparkles.paypal.payments.Amount;
import sparkles.paypal.payments.Item;
import sparkles.paypal.payments.ItemList;
import sparkles.paypal.payments.Payer;
import sparkles.paypal.payments.Payment;
import sparkles.paypal.payments.PaymentExecution;
import sparkles.paypal.payments.PaymentMethod;
import sparkles.paypal.payments.PaymentsApi;
import sparkles.paypal.payments.RedirectUrls;
import sparkles.paypal.payments.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@Slf4j
public class PaypalSandboxTests {

  @Test
  public void foo() throws Exception {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
      @Override public void log(String message) {
        log.info(message);
      }
    });
    logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

    final PaypalClient client = new PaypalClient.Builder()
      .baseUrl("https://api.sandbox.paypal.com/")
      .appClient(
        "AYY8ITH_oiNQnnNrVo4vjijlKihYZ_SZ_KXp5vXuFtDR-AQVqrzzMdoe9w2Z5fHPqevIwpr70Yw1Be67",
        "EG4LznSjt-CBlFjqtRs52Y_FU3Bz4vqCuCZH65aB7hUelMMBYoIxw1w_DzLRPGmBEZnYyXu9EBAqHBPB"
      )
      .logging(logging)
      .build();

    final PaymentsApi api = client.paymentsApi();

    final Response<Payment> payment = api.createPayment(new Payment()
      .intent("sale")
      .redirectUrls(new RedirectUrls()
        .returnUrl("https://foo.com/bar/confirmed-payment")
        .cancelUrl("https://foo.com/bar/cancel-payment"))
      .payer(new Payer()
        .paymentMethod(PaymentMethod.PAYPAL))
      .transactions(Arrays.asList(
        new Transaction()
          .amount(new Amount()
            .currency("EUR")
            .total("51.23"))
          .itemList(new ItemList()
            .items(Arrays.asList(
              new Item()
                .sku("a")
                .quantity("5")
                .price("10.00")
                .currency("EUR"),
              new Item()
                .sku("a")
                .quantity("1")
                .price("1.23")
                .currency("EUR")
            ))
            .shippingMethod("Götterbote"))
      ))).execute();

    final Response<Payment> execution = api.executePaymentById(
      payment.body().id(),
      new PaymentExecution()
        .payerId("123")).execute();

    assertThat(payment.code()).isEqualTo(200);
    assertThat(execution.code()).isEqualTo(200);

  }

}
