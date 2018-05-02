package sparkles.support.keycloak.jwk;

import com.squareup.moshi.Json;

public class JWK {
  public static final String KEY_ID = "kid";
  public static final String KEY_TYPE = "kty";
  public static final String ALGORITHM = "alg";
  public static final String PUBLIC_KEY_USE = "use";

  @Json(name = KEY_ID)
  public String keyId;

  @Json(name = KEY_TYPE)
  public String keyType;

  @Json(name = ALGORITHM)
  public String algorithm;

  @Json(name = PUBLIC_KEY_USE)
  public String publicKeyUse;
}
