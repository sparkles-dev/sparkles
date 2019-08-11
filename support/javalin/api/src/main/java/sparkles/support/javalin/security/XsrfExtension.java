package sparkles.support.javalin.security;

import io.javalin.Extension;
import io.javalin.Javalin;

public class XsrfExtension implements Extension {

  private XsrfExtension() {}

  @Override
  public void registerOnJavalin(Javalin app) {
    app.before(new XsrfFilter());
  }

  public static XsrfExtension create() {
    return new XsrfExtension();
  }

}
