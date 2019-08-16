package sparkles.support.javalin.jwt;

import io.javalin.core.security.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * A role provider should extract an app-level role from a JSON web token.
 */
@FunctionalInterface
public interface RoleProvider {

  Role role(Jws<Claims> fromClaims);
}
