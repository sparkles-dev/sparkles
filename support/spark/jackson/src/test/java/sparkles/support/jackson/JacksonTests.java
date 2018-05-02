package sparkles.support.jackson;

import java.util.UUID;
import java.time.LocalDateTime;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
import static sparkles.support.jackson.JacksonResponseTransformer.jacksonTransformer;

@RunWith(SparkTestRunner.class)
public class JacksonTests {

  @TestClient
  SparkHttpClient client;

  @TestApp
  static void testApp() {
    get("/", (req, res) -> "foobar", jacksonTransformer());

    get("/datetime", (req, res) -> new JacksonAnnotatedExample()
      .withDateTime(LocalDateTime.parse("2007-12-03T10:15:30")), jacksonTransformer());

    get("/uuid", (req, res) -> new JacksonAnnotatedExample()
      .withUuid(UUID.fromString("c14e1b3a-3a2b-11e8-b467-0ed5f89f718b")), jacksonTransformer());
  }

  @Test
  public void itShouldReturnAJsonString() throws Exception {
    Response response = client.newCall("GET", "").execute();
    assertThat(response.body().string()).isEqualTo("\"foobar\"");
  }

  @Test
  public void itShouldConvertLocalDateTime() throws Exception {
    Response response = client.newCall("GET", "datetime").execute();
    assertThat(response.body().string()).isEqualTo("{\n  \"dateTime\" : \"2007-12-03T10:15:30\"\n}");
  }

  @Test
  public void itShouldConvertUUID() throws Exception {
    Response response = client.newCall("GET", "uuid").execute();
    assertThat(response.body().string()).isEqualTo("{\n  \"uuid\" : \"c14e1b3a-3a2b-11e8-b467-0ed5f89f718b\"\n}");
  }

}
