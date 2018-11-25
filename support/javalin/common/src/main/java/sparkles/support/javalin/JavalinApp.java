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

  /**
   * Registers an {@link Extension} with the Javalin application.
   *
   * If the extension is a lambda expression or an anonymous class, it's considered an anonymous
   * extension.
   *
   * @param extension You're free to implement the extension as a class or a lambda expression
   * @return Self instance for fluent, method-chaining API
   */
  public JavalinApp register(Extension extension) {
    extension.addToJavalin(this);

    return this;
  }

  public static Set<Role> requires(Role... roles) {
    return Arrays.stream(roles).collect(Collectors.toSet());
  }

}
