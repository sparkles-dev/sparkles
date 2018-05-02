package sparkles.support.jwt;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public interface PublicKeyProvider {
  PublicKey publicKey() throws NoSuchAlgorithmException, InvalidKeySpecException;
}
