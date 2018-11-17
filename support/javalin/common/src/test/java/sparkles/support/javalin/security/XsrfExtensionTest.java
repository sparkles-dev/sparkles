package sparkles.support.javalin.security;

import io.javalin.Javalin;

import java.util.UUID;

import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;

import org.junit.Test;
import org.junit.runner.RunWith;

import sparkles.support.javalin.JavalinApp;
import sparkles.support.javalin.testing.JavalinTestRunner;
import sparkles.support.javalin.testing.TestApp;
import sparkles.support.javalin.testing.TestClient;
import sparkles.support.javalin.testing.HttpClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JavalinTestRunner.class)
public class XsrfExtensionTest {

  @TestClient
  public HttpClient http;

  @TestApp
  public Javalin create() {
    return JavalinApp.create()
      .register(XsrfExtension.create())
      .get("/", ctx -> ctx.result("Hello world"))
      .post("/", ctx -> ctx.status(201));
  }

  @Test
  public void get_itShouldAddAnXsrfCookie() throws Exception {
    assertThat(http.get("/").execute().header("Set-Cookie")).startsWith("XSRF-TOKEN=");
  }

  @Test
  public void post_itShouldRespond401WithoutToken() throws Exception {
    Response response = http.post("/", http.emptyBody()).execute();

    assertThat(response.code()).isEqualTo(401);
  }

  @Test
  public void post_itShouldRespond401WhenCookieIsMissing() throws Exception {
    Request request = http.newRequest("POST", "/", http.emptyBody()).newBuilder()
      .header("X-XSRF-TOKEN", UUID.randomUUID().toString())
      .build();
    Response response = http.send(request);

    assertThat(response.code()).isEqualTo(401);
  }

  @Test
  public void post_itShouldRespond401WhenHeaderIsMissing() throws Exception {
    Request request = http.newRequest("POST", "/", http.emptyBody()).newBuilder()
      .header("Cookie", "XSRF-TOKEN=" + UUID.randomUUID().toString())
      .build();
    Response response = http.send(request);

    assertThat(response.code()).isEqualTo(401);
  }

  @Test
  public void post_itShouldRespond201WithValidTokens() throws Exception {
    UUID token = UUID.randomUUID();
    Request request = http.newRequest("POST", "/", http.emptyBody()).newBuilder()
      .header("X-XSRF-TOKEN", token.toString())
      .header("Cookie", "XSRF-TOKEN=" + token.toString())
      .build();
    Response response = http.send(request);

    assertThat(response.code()).isEqualTo(201);
  }

}
