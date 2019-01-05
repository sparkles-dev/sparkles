package sparkles.support.javalin;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import io.javalin.Javalin;
import io.javalin.security.Role;

public class JavalinApp extends Javalin {

  protected JavalinApp() {
    super();
  }

  public static JavalinApp create() {
    return new JavalinApp();
  }

  public static Set<Role> requires(Role... roles) {
    return Arrays.stream(roles).collect(Collectors.toSet());
  }

}
