package sparkles.support.keycloak.jwk;

import com.squareup.moshi.Json;

public class RSAPublicJWK extends JWK {
  public static final String RSA = "RSA";
  public static final String RS256 = "RS256";

  public static final String MODULUS = "n";
  public static final String PUBLIC_EXPONENT = "e";

  @Json(name = MODULUS)
  public String modulus;

  @Json(name = PUBLIC_EXPONENT)
  public String publicExponent;
}
