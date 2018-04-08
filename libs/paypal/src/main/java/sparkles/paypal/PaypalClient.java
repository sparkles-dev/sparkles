package sparkles.paypal;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import sparkles.paypal.oauth2.AppAuthentication;
import sparkles.paypal.oauth2.TokenInterceptor;
import sparkles.paypal.payments.PaymentsApi;

public interface PaypalClient {
  public static final String SANDBOX = "https://api.sandbox.paypal.com/";
  public static final String LIVE = "https://api.paypal.com/";

  PaymentsApi paymentsApi();

  @Data
  static class PaypalClientImpl implements PaypalClient {
    private final Retrofit retrofit;

    @Override
    public PaymentsApi paymentsApi() {

      return retrofit.newBuilder()
        .baseUrl(retrofit.baseUrl().newBuilder()
          .addPathSegments("v1/payments/")
          .build())
        .build()
        .create(PaymentsApi.class);
    }
  }

  @Accessors(fluent = true)
  public static class Builder {

    @Setter
    private String baseUrl;

    @Setter
    private HttpLoggingInterceptor logging;

    private String clientId;
    private String secret;

    /**
     * Sets the PayPal app credentials.
     *
     * @param clientId
     * @param secret
     */
    public Builder paypalApp(String clientId, String secret) {
      this.clientId = clientId;
      this.secret = secret;

      return this;
    }

    /** Sets the sandbox base URL. */
    public Builder sandbox() {
      this.baseUrl = SANDBOX;

      return this;
    }

    /** Sets the live environment base URL. */
    public Builder live() {
      this.baseUrl = LIVE;

      return this;
    }

    public PaypalClient build() {
      if (Strings.isNullOrEmpty(baseUrl)) {
        throw new IllegalArgumentException("baseUrl must not be null or empty");
      }
      if (Strings.isNullOrEmpty(clientId)) {
        throw new IllegalArgumentException("clientId must not be null or empty");
      }
      if (Strings.isNullOrEmpty(secret)) {
        throw new IllegalArgumentException("secret must not be null or empty");
      }

      final OkHttpClient.Builder oAuthClient = new OkHttpClient.Builder();
      if (logging != null) {
        oAuthClient.addInterceptor(logging);
      }

      final AppAuthentication tokenAuth = new AppAuthentication.Builder()
        .baseUrl(HttpUrl.parse(baseUrl)
          .newBuilder()
          .addPathSegments("v1/oauth2")
          .build())
        .clientId(clientId)
        .secret(secret)
        .okHttpClient(oAuthClient.build())
        .build();

      oAuthClient.addNetworkInterceptor(new TokenInterceptor(tokenAuth));

      final Retrofit retrofit = new Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(baseUrl)
        .client(oAuthClient.build())
        .build();

      return new PaypalClientImpl(retrofit);
    }
  }

}
