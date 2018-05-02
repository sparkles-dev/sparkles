package sparkles.support.testing;

import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import sparkles.support.testing.SparkTestRunner;
import sparkles.support.testing.SparkHttpClient;
import sparkles.support.testing.TestClient;
import sparkles.support.testing.TestApp;

import static org.assertj.core.api.Assertions.assertThat;
import static spark.Spark.get;

@RunWith(SparkTestRunner.class)
public class StaticMethodTest {

  @TestClient
  private SparkHttpClient client;

  @TestApp
  public static void testApp() {
    get("/", (req, res) -> {
      return "foobar";
    });
  }

  @Test
  public void itRuns() throws Exception {
    Response response = client.newCall("GET", "").execute();
    assertThat(response.body().string()).isEqualTo("foobar");
  }

}
