package sparkles.support.jwt;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.Charset;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import spark.servlet.SparkApplication;
import sparkles.support.jwt.PublicKeyProvider;
import sparkles.support.jwt.SimplePublicKeyProvider;
import sparkles.support.testing.SparkTestRunner;
import sparkles.support.testing.SparkHttpClient;
import sparkles.support.testing.TestClient;
import sparkles.support.testing.TestApp;

import static org.assertj.core.api.Assertions.assertThat;
import static spark.Spark.get;
import static sparkles.support.jwt.JwtSupport.filterAuthenticatedRequests;

@RunWith(SparkTestRunner.class)
public class JwtTests {

  @TestClient
  SparkHttpClient client;

  @TestApp
  public static SparkApplication testApp = new SparkApplication() {

    @Override
    public void init() {

      try {
        // Read signing key
        String publicKey = Resources.toString(Resources.getResource("jwt/public.key"),
          Charset.forName("UTF-8"));

        // Enable jwt support
        filterAuthenticatedRequests(new SimplePublicKeyProvider(publicKey));
      } catch (IOException e) {
        throw new RuntimeException("Public Key for JWT support not readable. Initialization failed.", e);
      }

      get("/", (req, res) -> {
        return "foobar";
      });

      get("/bar", (req, res) -> {
        return "bar";
      });
    }
  };

  @Test
  public void itResponds400_noAuthorizationHeader() throws IOException {
    Response response = client.newCall("GET", "").execute();
    assertThat(response.code()).isEqualTo(400);
  }

  @Test
  public void itResponds400_authorizationHeaderWithoutBearerToken() throws IOException {
    Request request = client.newRequest("GET", "", null)
      .newBuilder()
      .header("Authorization", "no-bearer-token")
      .build();

    Response response = client.newCall(request).execute();
    assertThat(response.code()).isEqualTo(400);
  }

  @Test
  public void itResponds400_authorizationHeaderWithMalformedBearerToken() throws IOException {
    Request request = client.newRequest("GET", "", null)
      .newBuilder()
      .header("Authorization", "Bearer malformed-token")
      .build();

    Response response = client.newCall(request).execute();
    assertThat(response.code()).isEqualTo(400);
  }

  @Test
  public void itResponds401_authorizationHeaderWithInvalidBearerToken() throws IOException {
    String token = Resources.toString(Resources.getResource("jwt/invalid.token"),
      Charset.forName("UTF-8")).replaceAll("\\n", "").replaceAll(" ", "");

    Request request = client.newRequest("GET", "", null)
      .newBuilder()
      .header("Authorization", "Bearer " + token)
      .build();

    Response response = client.newCall(request).execute();
    assertThat(response.code()).isEqualTo(400);
  }

  @Test
  public void itResponds200_authorizationHeaderWithValidBearerToken() throws IOException {
    String validToken = Resources.toString(Resources.getResource("jwt/valid.token"),
      Charset.forName("UTF-8")).replaceAll("\\n", "").replaceAll(" ", "");

    Request request = client.newRequest("GET", "", null)
      .newBuilder()
      .header("Authorization", "Bearer " + validToken)
      .build();

    Response response = client.newCall(request).execute();
    assertThat(response.code()).isEqualTo(200);
  }

}
