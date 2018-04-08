package sparkles.paypal;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import sparkles.paypal.oauth2.OAuth2Api;
import sparkles.paypal.oauth2.TokenGrant;

@Data
public class TokenInterceptor implements Interceptor {

  private final Authentication authentication;

  private TokenGrant token;
  private long tokenExpiresAt;

  @Override
  public Response intercept(Interceptor.Chain chain) throws IOException {
    final Request originalRequest = chain.request();
    if (originalRequest.header("Authorization") != null) {
      return chain.proceed(originalRequest);
    }

    if (isTokenExpired()) {
      final retrofit2.Response<TokenGrant> response = authentication.getToken().execute();
      if (response.isSuccessful()) {
        updateToken(response.body());
      } else {
        return response.raw();
      }
    }

    return chain.proceed(originalRequest.newBuilder()
      .header("Authorization", String.format("%s %s", token.tokenType(), token.accessToken()))
      .build());

  }

  private void updateToken(TokenGrant token) {
    this.token = token;

    final long currentSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    tokenExpiresAt = TimeUnit.SECONDS.toMillis(currentSeconds + token.expiresIn());
  }

  private boolean isTokenExpired() {
    return System.currentTimeMillis() > tokenExpiresAt;
  }

  public static interface Authentication {
    Call<TokenGrant> getToken();
  }

  @Data
  public static class TokenAuthentication implements Authentication {
    private final OAuth2Api oAuth2Api;

    @Override
    public Call<TokenGrant> getToken() {
      return oAuth2Api.getAccessToken("client_credentials");
    }
  }

}
