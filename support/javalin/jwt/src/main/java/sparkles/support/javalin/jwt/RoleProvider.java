package sparkles.support.javalin.jwt;

import io.javalin.security.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@FunctionalInterface
public interface RoleProvider {

  Role role(Jws<Claims> fromClaims);
}
