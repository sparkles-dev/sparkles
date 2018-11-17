package sparkles.support.javalin;

import java.util.HashMap;
import java.util.Map;

import io.javalin.Javalin;

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

}
