package sparkles.paypal.oauth2;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

/**
 * Base URL (Sandbox): `https://api.sandbox.paypal.com/v1/oauth2/`
 *
 * @link https://developer.paypal.com/docs/api/overview/#authentication-and-authorization
 */
public interface OAuth2Api {

  @FormUrlEncoded
  @Headers("Accept: application/json")
  @POST("token")
  Call<TokenGrant> getAccessToken(
    @Field("grant_type") String grantType
  );

}
