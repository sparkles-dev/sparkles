package sparkles.support.persistence;

import java.util.Collections;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mapping.context.PersistentEntities;
import spark.Request;
import spark.Response;

import static spark.Spark.before;
import static spark.Spark.afterAfter;

public final class Auditing {
  private static final Logger LOG = LoggerFactory.getLogger(Auditing.class);

  public enum Strategy {
    GLOBAL,
    INHERITED_THREAD_LOCAL,
    THREAD_LOCAL;
  }

  private static AuditingHandler auditingHandler;
  private static StrategyHandler<Object> strategyHandler;

  private Auditing() {}

  public static <T> void enableAuditing(AuditorResolver<T> resolver) {
    enableAuditing(resolver, Strategy.INHERITED_THREAD_LOCAL);
  }

  public static <T> void enableAuditing(AuditorResolver<T> resolver, AuditingHandler handler) {
    enableAuditing(resolver, handler, Strategy.INHERITED_THREAD_LOCAL);
  }

  public static <T> void enableAuditing(AuditorResolver<T> resolver, Strategy strategy) {
    enableAuditing(resolver, new AuditingHandler(new PersistentEntities(Collections.emptyList())), strategy);
  }

  public static <T> void enableAuditing(AuditorResolver<T> resolver, AuditingHandler handler, Strategy strategy) {
    if (strategy == Strategy.INHERITED_THREAD_LOCAL) {
      strategyHandler = new InheritableThreadLocalStrategy();
    } else if (strategy == Strategy.GLOBAL) {
      strategyHandler = new GlobalStrategy();
    } else if (strategy == Strategy.THREAD_LOCAL) {
      strategyHandler = new ThreadLocalStrategy();
    } else {
      LOG.warn("No auditing strategy.");
    }

    auditingHandler = handler;

    before((request, response) -> {
      T auditor = resolver.resolve(request, response);
      strategyHandler.clear();

      @SuppressWarnings("unchecked")
      final Object value = auditor;
      strategyHandler.update(value);
    });

    afterAfter((request, response) -> {
      strategyHandler.clear();
    });
  }

  public static <T> AuditorAware<T> currentAuditor() {
    return new AuditorAware() {
      public Optional<T> getCurrentAuditor() {
        return (Optional<T>) strategyHandler.get();
      }
    };
  }

  public static AuditingHandler handler() {
    return auditingHandler;
  }

  static interface StrategyHandler<T> extends AuditorAware<T> {
    void clear();
    Optional<T> get();
    void update(T auditor);

    Optional<T> getCurrentAuditor();
  }

  static class InheritableThreadLocalStrategy implements StrategyHandler<Object> {
    private static final ThreadLocal<Object> holder = new InheritableThreadLocal<>();

    @Override
    public void clear() {
      holder.remove();
    }

    @Override
    public void update(Object auditor) {
      holder.set(auditor);
    }

    @Override
    public Optional<Object> get() {
      return Optional.ofNullable(holder.get());
    }

    @Override
    public Optional<Object> getCurrentAuditor() {
      return get();
    }
  }

  static class ThreadLocalStrategy implements StrategyHandler<Object> {
    private static final ThreadLocal<Object> holder = new ThreadLocal<>();

    @Override
    public void clear() {
      holder.remove();
    }

    @Override
    public void update(Object auditor) {
      holder.set(auditor);
    }

    @Override
    public Optional<Object> get() {
      return Optional.ofNullable(holder.get());
    }

    @Override
    public Optional<Object> getCurrentAuditor() {
      return get();
    }
  }

  static class GlobalStrategy implements StrategyHandler<Object> {
    private static Object holder;

    @Override
    public void clear() {
      holder = null;
    }

    @Override
    public void update(Object auditor) {
      holder = auditor;
    }

    @Override
    public Optional<Object> get() {
      return Optional.ofNullable(holder);
    }

    @Override
    public Optional<Object> getCurrentAuditor() {
      return get();
    }
  }
}
