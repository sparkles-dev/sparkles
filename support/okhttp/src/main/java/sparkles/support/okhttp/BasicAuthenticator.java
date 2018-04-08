package sparkles.support.okhttp;

import java.io.IOException;
import lombok.Data;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Route;
import okhttp3.Request;
import okhttp3.Response;

@Data
public class BasicAuthenticator implements Authenticator {

  private final String username;
  private final String password;

  @Override
  public Request authenticate(Route route, Response response) throws IOException {
    if (response.request().header("Authorization") != null) {
      return null; // Give up, we've already attempted to authenticate.
    }

    return response.request().newBuilder()
      .header("Authorization", Credentials.basic(username, password))
      .build();
  }
}
