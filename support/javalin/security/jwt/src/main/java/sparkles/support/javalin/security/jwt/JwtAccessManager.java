package sparkles.support.javalin.security.jwt;

import com.google.common.base.Strings;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.jsonwebtoken.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Set;

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
    // Public route
    if (permittedRoles.isEmpty()) {
      handler.handle(ctx);

      return;
    }

    // Secured route (one requiring a role)
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
