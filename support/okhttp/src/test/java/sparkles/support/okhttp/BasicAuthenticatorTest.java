package sparkles.support.okhttp;

import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicAuthenticatorTest {

  private final BasicAuthenticator authenticator = new BasicAuthenticator("foo", "bar");

  @Test
  public void itShouldAddBasicAuthHeader() throws Exception {
    final Request request = buildRequest();
    final Response response = buildResponse(request);

    final Request authenticated = authenticator.authenticate(null, response);

    assertThat(authenticated.header("Authorization")).isEqualTo("Basic Zm9vOmJhcg==");
  }

  @Test
  public void itShouldNotRetryAlreadyAuthenticatedRequests() throws Exception {
    final Request request = buildRequest().newBuilder()
      .header("Authorization", "foobar")
      .build();
    final Response response = buildResponse(request);

    final Request authenticated = authenticator.authenticate(null, response);

    assertThat(authenticated).isNull();
  }

  private static Request buildRequest() {
    return new Request.Builder()
      .url("https://foo.com/")
      .get()
      .build();
  }

  private static Response buildResponse(Request request) {
    return new Response.Builder()
      .request(request)
      .protocol(okhttp3.Protocol.HTTP_1_1)
      .code(401)
      .message("Unauthorized")
      .build();
  }

}
