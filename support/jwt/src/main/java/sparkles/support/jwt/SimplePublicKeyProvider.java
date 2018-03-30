package sparkles.support.jwt;

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
    byte[] publicBytes = Base64.getDecoder().decode(base64PublicKey);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey pubKey = keyFactory.generatePublic(keySpec);

    return pubKey;
  }

}
