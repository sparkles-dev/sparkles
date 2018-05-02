package sparkles.support.keycloak.jwk;

import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public class JWKParser {

  /** JSON web key being parsed */
  private JWK jwk;

  private JWKParser() {}

  public static JWKParser create() {
    return new JWKParser();
  }

  public JWKParser parse(String jwk) {
    try {
      this.jwk = new Moshi.Builder().build().adapter(RSAPublicJWK.class).fromJson(jwk);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return this;
  }

  public JWKParser jwk(JWK jwk) {
    this.jwk = jwk;

    return this;
  }

  public JWK getJwk() {
    return jwk;
  }

  public PublicKey toPublicKey() {
    String keyType = jwk.keyType;
    if (isKeyTypeSupported(keyType) && jwk instanceof RSAPublicJWK) {
      RSAPublicJWK rsaJwk = (RSAPublicJWK) jwk;

      final Base64.Decoder decoder = Base64.getUrlDecoder();
      final BigInteger modulus = new BigInteger(1, decoder.decode(rsaJwk.modulus));
      final BigInteger publicExponent = new BigInteger(1, decoder.decode(rsaJwk.publicExponent));

      try {
        return KeyFactory.getInstance("RSA")
          .generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      throw new RuntimeException("Unsupported keyType " + keyType);
    }
  }

  public boolean isKeyTypeSupported(String keyType) {
    return RSAPublicJWK.RSA.equals(keyType);
  }

}
