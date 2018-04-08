package sparkles.paypal.oauth2;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.JsonAdapter;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@RequiredArgsConstructor
@Slf4j
public class TokenAuthentication implements TokenInterceptor.Adapter {
  private final HttpUrl baseUrl;
  private final String clientId;
  private final String secret;
  private final OkHttpClient okHttpClient;

  private final JsonAdapter<TokenGrant> jsonAdapter = new Moshi.Builder().build()
    .adapter(TokenGrant.class);

  private TokenInterceptor.Response tokenResponse;

  @Override
  public TokenInterceptor.Response fetchToken() {
    try {
      updateAccessToken();
    } catch (IOException e) {
      log.error("Cannot fetch access token", e);
    }

    return tokenResponse;
  }

  @Override
  public boolean isTokenExpired() {
    if (tokenResponse != null) {
      // Verify ttl
      final long expiresIn = TimeUnit.SECONDS.toMillis(tokenResponse.grant().expiresIn());

      return System.currentTimeMillis() > (tokenResponse.response().sentRequestAtMillis() + expiresIn);
    } else {
      // No Response, need to fetch a new one
      return true;
    }
  }

  @Override
  public TokenGrant peekToken() {
    return tokenResponse != null ? tokenResponse.grant() : null;
  }

  private Request getAccessToken() {
    return new Request.Builder()
      .url(baseUrl.newBuilder()
        .addPathSegment("token")
        .build()
      )
      .post(new FormBody.Builder()
        .add("grant_type", "client_credentials")
        .build())
      .header("Accept", "application/json")
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("Authorization", Credentials.basic(clientId, secret))
      .build();
  }

  private void updateAccessToken() throws IOException {
    if (isTokenExpired()) {
      final Request request = getAccessToken();

      try (final Response response = okHttpClient.newCall(request).execute()) {
        if (response.isSuccessful()) {
          tokenResponse = new TokenInterceptor.Response(jsonAdapter.fromJson(response.body().source()), response);
        } else {
          tokenResponse = new TokenInterceptor.Response(null, response);
        }
      }
    }
  }

  @Accessors(fluent = true)
  public static class Builder {

    @Setter
    private HttpUrl baseUrl;

    @Setter
    private String clientId;

    @Setter
    private String secret;

    @Setter
    private OkHttpClient okHttpClient;

    public TokenAuthentication build() {

      return new TokenAuthentication(
        baseUrl,
        clientId,
        secret,
        okHttpClient
      );
    }
  }

}
