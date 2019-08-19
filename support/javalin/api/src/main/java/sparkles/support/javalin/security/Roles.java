package sparkles.support.javalin.security;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import io.javalin.core.security.Role;

public final class Roles {

  public static Set<Role> requires(Role... roles) {
    return Arrays.stream(roles).collect(Collectors.toSet());
  }

}
