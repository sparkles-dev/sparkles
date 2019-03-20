package sparkles.support.javalin;

import io.javalin.Javalin;

public class JavalinApp extends Javalin {

  protected JavalinApp() {
    super();
  }

  public static JavalinApp create() {
    return new JavalinApp();
  }

  public static void foo() {}

  public static void bar() {}
}
