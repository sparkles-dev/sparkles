package sparkles.support.replication;

import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ReplicationApi {

  /**
   * Expected response: 200 Ok with payload
   */
  @GET("replication/subscription/{id}")
  Call<ResponseBody> fetchData(@Path("id") String id);

  /**
   * Expected response: 201 Created
   * w/ header Location: /upstream/subscription/:id
   */
  @POST("replication/subscription")
  Call<Subscription> subscribe(@Body Subscription subscription);

  /**
   * Expected response: 204 No Content
   */
  @POST("replication/notification")
  Call<ResponseBody> notify(@Body Notification notification);

  @POST
  Call<ResponseBody> notify(@Url String url, @Body Notification notification);

  @GET
  Call<ResponseBody> get(@Url String url);

  class Builder {
    private String baseUrl;
    private OkHttpClient okHttpClient;

    public Builder() {}

    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;

      return this;
    }

    public Builder okHttpClient(OkHttpClient okHttpClient) {
      this.okHttpClient = okHttpClient;

      return this;
    }

    public ReplicationApi build() {
      if (okHttpClient == null) {
        okHttpClient = new OkHttpClient();
      }

      final Moshi moshi = new Moshi.Builder()
        .add(UuidAdapter.TYPE, new UuidAdapter())
        .build();

      return new Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient.newBuilder()
          .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
          .build()
        )
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(ReplicationApi.class);
    }
  }

}
