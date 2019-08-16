package sparkles.support.javalin.security.xsrf;

import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;
import sparkles.support.javalin.security.XsrfFilter;

public class XsrfPlugin implements Plugin {

  private XsrfPlugin() {}

  @Override
  public void apply(Javalin app) {
    app.before(new XsrfFilter());
  }

  public static XsrfPlugin create() {
    return new XsrfPlugin();
  }

}
