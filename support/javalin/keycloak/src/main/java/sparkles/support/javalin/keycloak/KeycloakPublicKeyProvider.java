package sparkles.support.javalin.keycloak;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import sparkles.support.javalin.jwt.PublicKeyProvider;
import sparkles.support.keycloak.JwksApi;
import sparkles.support.keycloak.jwk.JSONWebKeySet;
import sparkles.support.keycloak.jwk.JWKParser;
import sparkles.support.keycloak.jwk.RSAPublicJWK;

public class KeycloakPublicKeyProvider implements PublicKeyProvider {

  private final JwksApi jwksApi;
  private final String realm;

  private PublicKey publicKey = null;

  public KeycloakPublicKeyProvider(String baseUrl, String realm) {
    this.jwksApi = createJwksApi(baseUrl);
    this.realm = realm;
  }

  @Override
  public PublicKey publicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    if (publicKey == null) {
      try {
        final JSONWebKeySet keySet = jwksApi.getJsonWebKeySet(realm).execute().body();
        final JWKParser parser = JWKParser.create();

        assert keySet != null;
        for (final RSAPublicJWK key : keySet.keys) {
          if (parser.isKeyTypeSupported(key.keyType)) {
            publicKey = parser.jwk(key).toPublicKey();
          }
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return publicKey;
  }

  private static JwksApi createJwksApi(String baseUrl) {
    return new JwksApi.Builder().baseUrl(baseUrl).build();
  }
}
