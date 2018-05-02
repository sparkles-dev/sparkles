package sparkles.support.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.halt;

public class JwtSupport {

  /** @deprecated */
  public static void filterAuthenticatedRequest(PublicKeyProvider provider) {
    filterAuthenticatedRequests(provider);
  }

  public static void filterAuthenticatedRequests(PublicKeyProvider provider) {

    before(new JwtFilter(provider));
  }

  private static class JwtFilter implements Filter {
    private final PublicKeyProvider provider;

    private JwtFilter(PublicKeyProvider provider) {
      this.provider = provider;
    }

    @Override
    public void handle(Request request, Response response) {
      final String header = request.headers("Authorization");
      if (header == null || !header.startsWith("Bearer ")) {
        halt(400, "Please include header 'Authorization: Bearer <token>'.");
      }

      final String token = header.substring("Bearer ".length());
      // verify token
      try {
        Jwts.parser()
          .setSigningKey(provider.publicKey())
          .parseClaimsJws(token);
      } catch (SignatureException | InvalidKeySpecException | NoSuchAlgorithmException e) {
        halt(401, e.getMessage());
      } catch (JwtException e) {
        halt(400, e.getMessage());
      }
    }
  }

}
