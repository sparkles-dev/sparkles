package sparkles.paypal.payments;

import io.fabric8.mockwebserver.DefaultMockServer;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import sparkles.paypal.payments.PaymentExecution;

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
  public void foo() throws Exception {
    server.expect().withPath("/api/v1/users").andReturn(200, "admin").once();

    final PaymentExecution p = new PaymentExecution()
      .payerId("1123");

    p.payerId();
  }

}
