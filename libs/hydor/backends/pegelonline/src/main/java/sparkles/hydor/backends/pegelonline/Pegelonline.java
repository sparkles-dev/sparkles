package sparkles.hydor.backends.pegelonline;

import com.google.common.base.Strings;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public interface Pegelonline {
  public static final String LIVE = "https://www.pegelonline.wsv.de/webservices/rest-api/v2/";

  StationsApi stationsApi();

  static class PegelonlineImpl implements Pegelonline {
    private final Retrofit retrofit;

    PegelonlineImpl(Retrofit retrofit) {
      this.retrofit = retrofit;
    }

    @Override
    public StationsApi stationsApi() {

      return retrofit.create(StationsApi.class);
    }
  }

  public static class Builder {
    private String baseUrl;
    private HttpLoggingInterceptor logging;

    public Builder() {}

    /** Sets the live environment base URL. */
    public Builder live() {
      this.baseUrl = LIVE;

      return this;
    }

    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;

      return this;
    }

    public Builder logging(HttpLoggingInterceptor logging) {
      this.logging = logging;

      return this;
    }

    public Pegelonline build() {
      if (Strings.isNullOrEmpty(baseUrl)) {
        throw new IllegalArgumentException("baseUrl must not be null or empty");
      }

      final OkHttpClient.Builder okHttp = new OkHttpClient.Builder();
      if (logging != null) {
        okHttp.addInterceptor(logging);
      }

      final Retrofit retrofit = new Retrofit.Builder()
        .addConverterFactory(JacksonConverterFactory.create())
        .baseUrl(baseUrl)
        .client(okHttp.build())
        .build();

      return new PegelonlineImpl(retrofit);
    }
  }

}
