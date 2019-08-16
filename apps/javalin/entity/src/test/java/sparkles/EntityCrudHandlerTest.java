package sparkles;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;

import io.javalin.Javalin;

import okhttp3.Response;

import sparkles.entity.FooEntity;
import sparkles.support.javalin.testing.HttpClient;
import sparkles.support.javalin.testing.JavalinTestRunner;
import sparkles.support.javalin.testing.TestApp;
import sparkles.support.javalin.testing.TestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO: move to support package
 * https://github.com/tipsy/javalin/issues/709#issuecomment-521722888
 */
@RunWith(JavalinTestRunner.class)
@Ignore
public class EntityCrudHandlerTest {

  @TestClient
  public HttpClient testClient;

  @TestApp
  public Javalin setUpTestApp() {
    return new EntityApp().init();
  }

  @Test
  public void post_shouldCreateEntity() {
    final Response response = testClient
      .post("/stuff")
      .json("{ \"name\": \"foo\" }")
      .send();

    assertThat(response.code()).isEqualTo(200);
    assertThat(testClient.jsonResponse(FooEntity.class).id).isNotNull();
  }

  @Test
  public void getId_shouldReturnEntity() {
    testClient
      .post("/stuff")
      .json("{ \"name\": \"foo\" }")
      .send();
    UUID createdId = testClient.jsonResponse(FooEntity.class).id;

    final Response response = testClient
      .get("/stuff/" + createdId.toString())
      .send();
    final FooEntity responseEntity = testClient.jsonResponse(FooEntity.class);
    assertThat(response.code()).isEqualTo(200);
    assertThat(responseEntity.id).isEqualTo(createdId);
    assertThat(responseEntity.name).isEqualTo("foo");
  }

  @Test
  public void get_shouldReturnEntityList() {
    testClient
      .post("/stuff")
      .json("{ \"name\": \"foo\" }")
      .send();
    testClient
      .post("/stuff")
      .json("{ \"name\": \"bar\" }")
      .send();

    final Response response = testClient
      .get("/stuff")
      .send();

    @SuppressWarnings("unchecked")
    final List<FooEntity> responseEntities = testClient.jsonResponse(List.class);
    assertThat(response.code()).isEqualTo(200);
    assertThat(responseEntities.size()).isGreaterThan(2);
  }

}
