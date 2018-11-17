package sparkles.support.javalin.spring.data;

import sparkles.support.javalin.Extension;
import sparkles.support.javalin.JavalinApp;

public class AuditingExtension<T> implements Extension {

  private final AuditorResolver<T> resolver;

  private AuditingExtension(AuditorResolver<T> resolver) {
    this.resolver = resolver;
  }

  @Override
  public void addToJavalin(JavalinApp app) {

    app.before((ctx) -> {
        T currentAuditor = resolver.resolve(ctx);

        Auditing.getStrategy().resolveCurrentContext().clearCurrentAuditor();
        Auditing.getStrategy().resolveCurrentContext().setCurrentAuditor(currentAuditor);

      })
      .after((ctx) -> {

        Auditing.getStrategy().resolveCurrentContext().clearCurrentAuditor();

      });

  }

  public static <T> AuditingExtension<T> create(AuditorResolver<T> resolver) {
    return new AuditingExtension(resolver);
  }

}
