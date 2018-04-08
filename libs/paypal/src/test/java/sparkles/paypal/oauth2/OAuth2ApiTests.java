package sparkles.paypal.oauth2;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import sparkles.support.okhttp.BasicAuthenticator;
import sparkles.paypal.oauth2.AppAuthentication;
import sparkles.paypal.oauth2.TokenInterceptor;
import sparkles.paypal.oauth2.OAuth2Api;
import sparkles.paypal.oauth2.OAuth2ApiTokenAdapter;

public class OAuth2ApiTests {

  public static void foo() {
    final String baseUrl = "https://api.sandbox.paypal.com/";
    final String clientId = "foo";
    final String secret = "bar";

    // OAuth2Api via basic auth
    final OAuth2Api oAuth2Api = new Retrofit.Builder()
      .addConverterFactory(MoshiConverterFactory.create())
      .baseUrl(baseUrl + "v1/oauth2/")
      .client(new OkHttpClient.Builder()
        .authenticator(new BasicAuthenticator(clientId, secret))
        //.addInterceptor(logging)
        .build())
      .build()
      .create(OAuth2Api.class);

    // Token-based authentication
    final OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
      .addInterceptor(new TokenInterceptor(new OAuth2ApiTokenAdapter(oAuth2Api)));

  }

}
