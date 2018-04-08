package sparkles.paypal.oauth2;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Response;

@Data
public class OAuth2ApiTokenAdapter implements TokenInterceptor.Adapter {
  private final OAuth2Api oAuth2Api;

  private Response<TokenGrant> localResponse;

  @Override
  public TokenInterceptor.Response fetchToken() {
    try {
      localResponse = oAuth2Api.getAccessToken("client_credentials").execute();

      if (localResponse.isSuccessful()) {
        return new TokenInterceptor.Response(localResponse.body(), localResponse.raw());
      } else {
        return new TokenInterceptor.Response(null, localResponse.raw());
      }
    } catch (IOException e) {
      return null;
    }
  }

  @Override
  public boolean isTokenExpired() {
    if (localResponse != null && localResponse.isSuccessful()) {
      // Verify ttl
      final long expiresIn = TimeUnit.SECONDS.toMillis(localResponse.body().expiresIn());

      return System.currentTimeMillis() > (localResponse.raw().sentRequestAtMillis() + expiresIn);
    } else {
      return true;
    }
  }

  @Override
  public TokenGrant peekToken() {
    return localResponse != null ? localResponse.body() : null;
  }

}
