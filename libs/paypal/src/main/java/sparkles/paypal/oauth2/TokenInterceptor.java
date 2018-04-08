package sparkles.paypal.oauth2;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import sparkles.paypal.oauth2.OAuth2Api;
import sparkles.paypal.oauth2.TokenGrant;

@Data
public class TokenInterceptor implements Interceptor {

  private final Adapter adapter;

  @Override
  public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
    final Request originalRequest = chain.request();
    if (originalRequest.header("Authorization") != null) {
      return chain.proceed(originalRequest);
    }

    TokenGrant token;
    if (adapter.isTokenExpired()) {
      final TokenInterceptor.Response response = adapter.fetchToken();
      if (response.response().isSuccessful()) {
        token = response.grant();
      } else {
        return response.response();
      }
    } else {
      token = adapter.peekToken();
    }

    return chain.proceed(originalRequest.newBuilder()
      .header("Authorization", String.format("%s %s", token.tokenType(), token.accessToken()))
      .build());
  }

  @RequiredArgsConstructor
  @Accessors(fluent = true)
  public static class Response {

    @Getter
    private final TokenGrant grant;

    @Getter
    private final okhttp3.Response response;
  }

  public static interface Adapter {
    Response fetchToken();

    boolean isTokenExpired();

    TokenGrant peekToken();
  }

}
