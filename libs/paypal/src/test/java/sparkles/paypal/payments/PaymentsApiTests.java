package sparkles.paypal.payments;

import io.fabric8.mockwebserver.DefaultMockServer;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import retrofit2.Response;
import sparkles.paypal.PaypalClient;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentsApiTests {

  private final DefaultMockServer server = new DefaultMockServer();

  @Before
  public void start() {
    server.start();
  }

  @After
  public void shutdown() {
    server.shutdown();
  }

  @Test
  @Ignore
  public void executePaymentById_shouldPost() throws Exception {
    server.expect().withPath("/payment/1123/execute").andReturn(200, "{\"id\":\"1123\"}").once();

    final PaymentExecution p = new PaymentExecution()
      .payerId("1123");

    final PaymentsApi api = new PaypalClient.Builder()
      .baseUrl(server.url("/"))
      .paypalApp("test", "test")
      .build()
      .paymentsApi();

    final Response<Payment> paymentResponse = api.executePaymentById(p.payerId(), p).execute();

    assertThat(paymentResponse.code()).isEqualTo(200);

    final Payment payment = paymentResponse.body();
    assertThat(payment).isNotNull();
    assertThat(payment.id()).isEqualTo("1123");
  }

}
