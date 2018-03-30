package sparkles.support.jwt;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import static spark.Spark.before;
import static spark.Spark.halt;

public class JwtSupport {

  public static void filterAuthenticatedRequest(PublicKeyProvider provider) {

    before((req, res) -> {
      final String header = req.headers("Authorization");
      if (header == null) {
          halt(401);
      }

      if (!header.startsWith("Bearer ")) {
          halt(401);
      }
      final String token = header.substring("Bearer ".length());

      // verify token
      try {
        Jwts.parser()
          .setSigningKey(provider.publicKey())
          .parseClaimsJws(token);
      } catch (SignatureException | InvalidKeySpecException | NoSuchAlgorithmException e) {
        halt(401, e.getMessage());
      }
    });
  }
}
