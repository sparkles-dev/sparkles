package sparkles.support.javalin.security.jwt;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

@FunctionalInterface
public interface PublicKeyProvider {

  PublicKey publicKey() throws NoSuchAlgorithmException, InvalidKeySpecException;
}
