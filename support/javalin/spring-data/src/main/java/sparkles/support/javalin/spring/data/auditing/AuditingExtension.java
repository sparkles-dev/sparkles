package sparkles.support.javalin.spring.data.auditing;

import io.javalin.Extension;
import io.javalin.Javalin;

public class AuditingExtension<T> implements Extension {

  private final AuditorResolver<T> resolver;

  private AuditingExtension(AuditorResolver<T> resolver) {
    this.resolver = resolver;
  }

  @Override
  public void registerOnJavalin(Javalin app) {

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
