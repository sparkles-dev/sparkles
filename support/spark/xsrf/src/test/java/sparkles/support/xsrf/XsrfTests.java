package sparkles.support.xsrf;

import java.util.UUID;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import spark.servlet.SparkApplication;
import sparkles.support.testing.SparkTestRunner;
import sparkles.support.testing.SparkHttpClient;
import sparkles.support.testing.TestClient;
import sparkles.support.testing.TestApp;

import static org.assertj.core.api.Assertions.assertThat;
import static spark.Spark.get;
import static spark.Spark.post;
import static sparkles.support.xsrf.XsrfSupport.enableXsrf;

@RunWith(SparkTestRunner.class)
public class XsrfTests {

  @TestApp
  private static void testApp() {
    enableXsrf();

    get("/", (req, res) -> "foo");
    post("/", (req, res) -> "bar");
  }

  @TestClient
  private SparkHttpClient client;

  @Test
  public void itSetsXsrfTokenCookie_GET() throws Exception {
    final Call call = client.get("");
    final Response response = call.execute();
    final Cookie cookie = Cookie.parse(call.request().url(), response.header("Set-Cookie"));

    assertThat(cookie.name()).isEqualTo("XSRF-TOKEN");
    assertThat(cookie.persistent()).isFalse();
    assertThat(cookie.value()).isNotEmpty();
    assertThat(cookie.httpOnly()).isFalse();
    // assertThat(cookie.secure()).isTrue();
  }

  @Test
  public void itResponds401WithoutDoubleSubmittedCookie_POST() throws Exception {
    final Response response = client.post("", RequestBody.create(null, "")).execute();
    assertThat(response.code()).isEqualTo(401);
  }

  @Test
  public void itResponds200WithDoubleSubmittedCookie_POST() throws Exception {
    final String token = UUID.randomUUID().toString();
    final Request request = client.newRequest("POST", "", RequestBody.create(null, ""))
      .newBuilder()
      .header("X-XSRF-TOKEN", token)
      .header("Cookie", "XSRF-TOKEN=" + token)
      .build();
    final Response response = client.newCall(request).execute();
    assertThat(response.code()).isEqualTo(200);
  }

}
