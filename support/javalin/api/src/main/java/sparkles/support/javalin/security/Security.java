package sparkles.support.javalin.security;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import io.javalin.security.Role;

public class Security {

  public static Set<Role> requires(Role... roles) {
    return Arrays.stream(roles).collect(Collectors.toSet());
  }
}
