package sparkles.support.javalin.security;

import io.javalin.Javalin;

import java.util.UUID;

import okhttp3.Response;

import org.junit.Test;
import org.junit.runner.RunWith;

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
    return Javalin.create(cfg -> {
        cfg.registerPlugin(XsrfExtension.create());
      })
      .get("/", ctx -> ctx.result("Hello world"))
      .post("/", ctx -> ctx.status(201));
  }

  @Test
  public void get_itShouldAddAnXsrfCookie() {
    assertThat(http.get("/").send().header("Set-Cookie")).startsWith("XSRF-TOKEN=");
  }

  @Test
  public void post_itShouldRespond401WithoutToken() {
    final Response response = http.post("/").emptyBody().send();

    assertThat(response.code()).isEqualTo(401);
  }

  @Test
  public void post_itShouldRespond401WhenCookieIsMissing() {
    final Response response = http
      .post("/")
      .header("X-XSRF-TOKEN", UUID.randomUUID().toString())
      .emptyBody()
      .send();

    assertThat(response.code()).isEqualTo(401);
  }

  @Test
  public void post_itShouldRespond401WhenHeaderIsMissing() {
    final Response response = http
      .post("/")
      .header("Cookie", "XSRF-TOKEN=" + UUID.randomUUID().toString())
      .emptyBody()
      .send();

    assertThat(response.code()).isEqualTo(401);
  }

  @Test
  public void post_itShouldRespond201WithValidTokens() {
    final UUID token = UUID.randomUUID();
    final Response response = http
      .post("/")
      .header("X-XSRF-TOKEN", token.toString())
      .header("Cookie", "XSRF-TOKEN=" + token.toString())
      .emptyBody()
      .send();

    assertThat(response.code()).isEqualTo(201);
  }

}
