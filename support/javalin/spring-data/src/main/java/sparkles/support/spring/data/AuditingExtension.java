package sparkles.support.spring.data;

import sparkles.support.javalin.Extension;
import sparkles.support.javalin.JavalinApp;

public class AuditingExtension<T> implements Extension {

  private final AuditorResolver<T> resolver;

  public AuditingExtension(AuditorResolver<T> resolver) {
    this.resolver = resolver;
  }

  @Override
  public void register(JavalinApp app) {

    app.before((ctx) -> {
        T currentAuditor = resolver.resolve(ctx);

        Auditing.getStrategy().resolveCurrentContext().clearCurrentAuditor();
        Auditing.getStrategy().resolveCurrentContext().setCurrentAuditor(currentAuditor);

      })
      .after((ctx) -> {

        Auditing.getStrategy().resolveCurrentContext().clearCurrentAuditor();

      });

  }

}
