package sparkles.support.javalin;

import io.javalin.Javalin;
import io.javalin.security.Role;
import sparkles.support.common.Environment;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class JavalinApp extends Javalin {
  static {
    System.setProperty(org.slf4j.impl.SimpleLogger.LOG_KEY_PREFIX + "sparkles", Environment.logLevel());
  }

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
