package sparkles;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;

import io.javalin.Javalin;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import sparkles.support.javalin.testing.HttpClient;
import sparkles.support.javalin.testing.JavalinTestRunner;
import sparkles.support.javalin.testing.TestApp;
import sparkles.support.javalin.testing.TestClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JavalinTestRunner.class)
public class StuffCrudHandlerTest {

  @TestClient
  public HttpClient testClient;

  @TestApp
  public Javalin setUpTestApp() {
    return new StuffApp().init();
  }

  @Test
  public void post_shouldCreateEntity() {
    testClient.enableLogging(HttpLoggingInterceptor.Level.BODY);
    final Response response = testClient
      .post("/foo")
      .json("{ \"name\": \"foo\" }")
      .send();

    assertThat(response.code()).isEqualTo(200);
    assertThat(testClient.jsonResponse(StuffEntity.class).id).isNotNull();
  }

  @Test
  public void getId_shouldReturnEntity() {
    testClient
      .post("/stuff")
      .json("{ \"name\": \"foo\" }")
      .send();
    UUID createdId = testClient.jsonResponse(StuffEntity.class).id;

    final Response response = testClient
      .get("/stuff/" + createdId.toString())
      .send();
    final StuffEntity responseEntity = testClient.jsonResponse(StuffEntity.class);
    assertThat(response.code()).isEqualTo(200);
    assertThat(responseEntity.id).isEqualTo(createdId);
    assertThat(responseEntity.name).isEqualTo("foo");
  }

  @Test
  public void get_shouldReturnEntityList() {
    testClient
      .post("/foo")
      .json("{ \"name\": \"foo\" }")
      .send();
    testClient
      .post("/foo")
      .json("{ \"name\": \"bar\" }")
      .send();

    final Response response = testClient
      .get("/foo")
      .send();
    // final List<StuffEntity> responseEntities = testClient.jsonResponse(List.class);
    assertThat(response.code()).isEqualTo(200);
    assertThat(testClient.stringResponse()).isNull();
  }

}
