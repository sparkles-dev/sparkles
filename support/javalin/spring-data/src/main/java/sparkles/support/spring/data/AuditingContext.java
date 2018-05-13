package sparkles.support.spring.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.auditing.AuditingHandler;

public class AuditingContext<T> {
  private static final Logger LOG = LoggerFactory.getLogger(AuditingContext.class);

  private Auditing.AuditorAwareStrategy<T> aware;
  private final AuditingHandler handler;
  private final AuditorResolver<T> resolver;

  AuditingContext(AuditingHandler handler, AuditorResolver<T> resolver) {
    this.handler = handler;
    this.resolver = resolver;
  }

  public void setStrategy(Auditing.Strategy strategy) {
    if (strategy == Auditing.Strategy.INHERITED_THREAD_LOCAL) {
      aware = new Auditing.InheritableThreadLocalStrategy<T>();
    } else if (strategy == Auditing.Strategy.GLOBAL) {
      aware = new Auditing.GlobalStrategy<T>();
    } else if (strategy == Auditing.Strategy.THREAD_LOCAL) {
      aware = new Auditing.ThreadLocalStrategy<T>();
    } else {
      LOG.warn("No auditing strategy.");
    }
  }

  public Auditing.AuditorAwareStrategy<T> getAware() {
    return aware;
  }

  public AuditingHandler getHandler() {
    return handler;
  }

  public AuditorResolver<T> getResolver() {
    return resolver;
  }
}
