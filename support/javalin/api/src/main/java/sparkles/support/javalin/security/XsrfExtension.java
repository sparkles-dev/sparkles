package sparkles.support.javalin.security;

import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;

public class XsrfExtension implements Plugin {

  private XsrfExtension() {}

  @Override
  public void apply(Javalin app) {
    app.before(new XsrfFilter());
  }

  public static XsrfExtension create() {
    return new XsrfExtension();
  }

}
