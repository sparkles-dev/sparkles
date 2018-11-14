package sparkles.support.javalin.testing;

import io.javalin.Javalin;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JavalinTestRunner.class)
public class JavalinApplicationTest {

  @TestClient
  private HttpClient client;

  @TestApp
  private Javalin testApp() {
    return Javalin.create()
      .get("/", (ctx) -> ctx.res.getWriter().print("foobar"))
      .start();
  }

  @Test
  public void itRuns() throws Exception {
    Response response = client.newCall("GET", "").execute();
    assertThat(response.body().string()).isEqualTo("foobar");
  }

}
