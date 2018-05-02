package sparkles.support.keycloak;

import sparkles.support.keycloak.jwk.JSONWebKeySet;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JwksApi {

  @GET(Keycloak.JWKS_URL)
  Call<JSONWebKeySet> getJsonWebKeySet(@Path("realm-name") String realm);

  class Builder {
    private String baseUrl;

    public Builder() {}

    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;

      return this;
    }

    public JwksApi build() {
      return new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(JwksApi.class);
    }

  }

}
