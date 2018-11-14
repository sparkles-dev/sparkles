package sparkles.support.javalin.jwt;

import com.google.common.base.Strings;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Set;

import io.javalin.Context;
import io.javalin.Handler;
import io.javalin.security.AccessManager;
import io.javalin.security.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class JwtAccessManager implements AccessManager {
  private static final String CONTEXT_CLAIMS = "JWS";
  private static final String BEARER = "Bearer ";

  private final PublicKeyProvider publicKeyProvider;
  private final RoleProvider roleProvider;

  public JwtAccessManager(PublicKeyProvider publicKeyProvider) {
    this(publicKeyProvider, null);
  }

  public JwtAccessManager(PublicKeyProvider publicKeyProvider, RoleProvider roleProvider) {
    this.publicKeyProvider = publicKeyProvider;
    this.roleProvider = roleProvider;
  }

  @Override
  public void manage(Handler handler, Context ctx, Set<Role> permittedRoles) throws Exception {
    final String authorizationHeader = ctx.header("Authorization");

    if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(BEARER)) {
      ctx.status(400).result("Please include 'Authorization: Bearer <token>'.");
    } else {
      final String token = authorizationHeader.substring(BEARER.length());

      try {
        // Verify token signature
        final Jws<Claims> claims = Jwts.parser()
          .setSigningKey(publicKeyProvider.publicKey())
          .parseClaimsJws(token);

        // Resolve a security role (in the application domain) from information in the token
        if (roleProvider != null) {
          final Role role = roleProvider.role(claims);

          if (permittedRoles.contains(role)) {
            ctx.attribute(CONTEXT_CLAIMS, claims);
            handler.handle(ctx);

            return;
          }
        }

        ctx.status(401).result("Unauthorized.");
      } catch (SignatureException | InvalidKeySpecException | NoSuchAlgorithmException e) {
        ctx.status(401).result(e.getMessage());
      } catch (JwtException e) {
        ctx.status(400).result(e.getMessage());
      }
    }
  }

}
