package sparkles.support.javalin.security;

import sparkles.support.javalin.Extension;
import sparkles.support.javalin.JavalinApp;

public class XsrfExtension implements Extension {

  private XsrfExtension() {}

  @Override
  public void addToJavalin(JavalinApp app) {
    app.before(new XsrfFilter());
  }

  public static XsrfExtension create() {
    return new XsrfExtension();
  }

}
