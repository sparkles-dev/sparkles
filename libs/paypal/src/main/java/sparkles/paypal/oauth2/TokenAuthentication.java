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

public interface TokenAuthentication {

  TokenGrant peekToken();

  TokenGrant fetchToken();

  @Data
  static class TokenResponse {
    public final Response response;
    public final TokenGrant token;
  }

  @RequiredArgsConstructor
  @Slf4j
  static class TokenAuthenticationImpl implements TokenAuthentication, Interceptor {
    private final HttpUrl baseUrl;
    private final String clientId;
    private final String secret;
    private final OkHttpClient okHttpClient;

    private final JsonAdapter<TokenGrant> tokenAdapter = new Moshi.Builder().build()
      .adapter(TokenGrant.class);

    private TokenResponse tokenResponse;

    @Override
    public TokenGrant peekToken() {
      return tokenResponse != null ? tokenResponse.token : null;
    }

    @Override
    public TokenGrant fetchToken() {
      try {
        updateAccessToken();
      } catch (IOException e) {
        log.error("Cannot fetch access token", e);
      }

      return peekToken();
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
            tokenResponse = new TokenResponse(response, tokenAdapter.fromJson(response.body().source()));
          } else {
            tokenResponse = new TokenResponse(response, null);
          }
        }
      }
    }

    private boolean isTokenExpired() {
      if (tokenResponse != null) {
        // Verify ttl
        final long expiresIn = TimeUnit.SECONDS.toMillis(tokenResponse.token.expiresIn());

        return System.currentTimeMillis() > (tokenResponse.response.sentRequestAtMillis() + expiresIn);
      } else {
        // No Response, need to fetch a new one
        return true;
      }
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
      final Request originalRequest = chain.request();
      if (originalRequest.header("Authorization") != null) {
        return chain.proceed(originalRequest);
      }

      updateAccessToken();
      if (tokenResponse != null && tokenResponse.token != null) {
        final TokenGrant token = tokenResponse.token;

        return chain.proceed(originalRequest.newBuilder()
          .header("Authorization", String.format("%s %s", token.tokenType(), token.accessToken()))
          .build());
      } else if (tokenResponse != null) {
        return tokenResponse.response;
      } else {
        return chain.proceed(originalRequest);
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

    public OkHttpClient build() {
      return okHttpClient.newBuilder()
        .addNetworkInterceptor(new TokenAuthenticationImpl(
          baseUrl,
          clientId,
          secret,
          okHttpClient
        ))
        .build();
    }
  }

}
