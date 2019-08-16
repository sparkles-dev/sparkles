package sparkles.support.javalin.springdata.auditing;

import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;

public class AuditingExtension<T> implements Plugin {

  private final AuditorResolver<T> resolver;

  private AuditingExtension(AuditorResolver<T> resolver) {
    this.resolver = resolver;
  }

  @Override
  public void apply(Javalin app) {

    app.before((ctx) -> {
        T currentAuditor = resolver.resolve(ctx);

        @SuppressWarnings("unchecked")
        final AuditingContext<T> audit = (AuditingContext<T>) Auditing.getStrategy().resolveCurrentContext();
        audit.clearCurrentAuditor();
        audit.setCurrentAuditor(currentAuditor);

      })
      .after((ctx) -> {

        Auditing.getStrategy().resolveCurrentContext().clearCurrentAuditor();

      });

  }

  public static <T> AuditingExtension<T> create(AuditorResolver<T> resolver) {
    return new AuditingExtension<T>(resolver);
  }

}
