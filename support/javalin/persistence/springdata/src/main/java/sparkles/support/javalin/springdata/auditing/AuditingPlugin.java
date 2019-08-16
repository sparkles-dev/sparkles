package sparkles.support.javalin.springdata.auditing;

import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;

public class AuditingPlugin<T> implements Plugin {

  private final AuditorResolver<T> resolver;

  private AuditingPlugin(AuditorResolver<T> resolver) {
    this.resolver = resolver;
  }

  @Override
  public void apply(Javalin app) {

    app
      .before((ctx) -> {
        T currentAuditor = resolver.resolve(ctx);

        @SuppressWarnings("unchecked")
        final ContextAware<T> audit = (ContextAware<T>) Auditing.getStrategy().resolveCurrentContext();
        audit.clearCurrentAuditor();
        audit.setCurrentAuditor(currentAuditor);

      })
      .after((ctx) -> {

        Auditing.getStrategy().resolveCurrentContext().clearCurrentAuditor();

      });

  }

  public static <T> AuditingPlugin<T> create(AuditorResolver<T> resolver) {
    return new AuditingPlugin<T>(resolver);
  }

}
