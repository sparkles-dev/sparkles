package sparkles.support.javalin.springdata.auditing;

public final class Auditing {
  private static Strategy contextStrategy = Strategy.INHERITED_THREAD_LOCAL;

  interface ContextResolvingStrategy<T> {
    ContextAware<T> resolveCurrentContext();
  }

  public enum Strategy implements ContextResolvingStrategy {
    GLOBAL {
      private ContextAware holder; // = new ContextAware(new AuditingHandler());

      @Override
      public ContextAware resolveCurrentContext() {
        return holder;
      }
    },
    INHERITED_THREAD_LOCAL {
      private final ThreadLocal<ContextAware> holder = new InheritableThreadLocal<>();

      @Override
      public ContextAware resolveCurrentContext() {
        ContextAware ctx = holder.get();
        if (ctx == null) {
          ctx = new ContextAware();
          holder.set(ctx);
        }

        return ctx;
      }
    },
    THREAD_LOCAL {
      private final ThreadLocal<ContextAware> holder = new ThreadLocal<>();

      @Override
      public ContextAware resolveCurrentContext() {
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
