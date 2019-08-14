package sparkles.support.javalin.jwt;

import io.javalin.core.security.Role;

/** Roles obtained from a JSON web token */
public enum JwtRoles implements Role {
  ANYONE
}
