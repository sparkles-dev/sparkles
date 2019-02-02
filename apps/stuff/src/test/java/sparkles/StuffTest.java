package sparkles;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import io.javalin.Javalin;
import okhttp3.Response;
import sparkles.support.javalin.testing.HttpClient;
import sparkles.support.javalin.testing.JavalinTestRunner;
import sparkles.support.javalin.testing.TestApp;
import sparkles.support.javalin.testing.TestClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JavalinTestRunner.class)
public class StuffTest {

  @TestClient
  public HttpClient testClient;

  @TestApp
  public Javalin setUpTestApp() {
    return new StuffApp().init();
  }

  @Test
  public void post_shouldRespond200() throws IOException {
    final Response response = testClient
      .post("/")
      .emptyBody()
      .send();

    assertThat(response.code()).isEqualTo(201);

    System.out.println(response.body().string());
  }

}
