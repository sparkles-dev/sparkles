package sparkles.support.keycloak.jwk;

import com.squareup.moshi.Json;

public class JSONWebKeySet {

  @Json(name = "keys")
  public RSAPublicJWK[] keys;
}
