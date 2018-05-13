package sparkles.support.spring.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Auditing {
  private static final Logger LOG = LoggerFactory.getLogger(Auditing.class);
  private static Strategy contextStrategy = Strategy.INHERITED_THREAD_LOCAL;

  interface ContextResolvingStrategy<T> {
    AuditingContext<T> resolveCurrentContext();
  }

  public enum Strategy implements ContextResolvingStrategy {
    GLOBAL {
      private AuditingContext holder; // = new AuditingContext(new AuditingHandler());

      @Override
      public AuditingContext resolveCurrentContext() {
        return holder;
      }
    },
    INHERITED_THREAD_LOCAL {
      private final ThreadLocal<AuditingContext> holder = new InheritableThreadLocal<>();

      @Override
      public AuditingContext resolveCurrentContext() {
        return holder.get();
      }
    },
    THREAD_LOCAL {
      private final ThreadLocal<AuditingContext> holder = new ThreadLocal<>();

      @Override
      public AuditingContext resolveCurrentContext() {
        return holder.get();
      }
    };
  }

  public static void setStrategy(Auditing.Strategy strategy) {
    contextStrategy = strategy;
  }

  public static Strategy getStrategy() {
    return contextStrategy;
  }

}
