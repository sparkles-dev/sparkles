package sparkles.replica;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import sparkles.replica.collection.CollectionApi;
import sparkles.support.javalin.testing.HttpClient;
import sparkles.support.javalin.testing.JavalinTestRunner;
import sparkles.support.javalin.testing.TestApp;
import sparkles.support.javalin.testing.TestClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JavalinTestRunner.class)
@Slf4j
public class CollectionApiTest {

  @TestApp
  private Javalin app = TestCommons.createTestApp(log, cfg -> {
    cfg.registerPlugin(new CollectionApi());
  });

  @TestClient
  private HttpClient client;

  @Test
  public void collections_POST() {
    // POST: create collection
    client.post("/collection")
      .json("{\"name\":\"foo\"}")
      .send();
    assertThat(client.response().code()).isEqualTo(201);
    assertThat(client.response().header("Location")).isNotEmpty();

    // HEAD: collection should exist
    client.head(client.response().header("Location")).send();
    assertThat(client.response().code()).isEqualTo(204);
  }

}
