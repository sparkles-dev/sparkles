package sparkles.support.javalin.security.jwt;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SimplePublicKeyProvider implements PublicKeyProvider {

  private final String base64PublicKey;

  public SimplePublicKeyProvider(String publicKey) {
    this.base64PublicKey = publicKey
      .replaceAll("\\n", "")
      .replace("-----BEGIN PUBLIC KEY-----", "")
      .replace("-----END PUBLIC KEY-----", "");
  }

  public PublicKey publicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    final byte[] publicBytes = Base64.getDecoder().decode(base64PublicKey);
    final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
    final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

    return keyFactory.generatePublic(keySpec);
  }

}
