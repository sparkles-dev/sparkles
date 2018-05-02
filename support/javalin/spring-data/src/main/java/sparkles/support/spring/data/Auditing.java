package sparkles.support.spring.data;

import java.util.Collections;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mapping.context.PersistentEntities;

public final class Auditing {
  private static final Logger LOG = LoggerFactory.getLogger(Auditing.class);

  public enum Strategy {
    GLOBAL,
    INHERITED_THREAD_LOCAL,
    THREAD_LOCAL;
  }

  private static AuditingAdapter<?> adapter;

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
    adapter = new AuditingAdapter(handler, resolver);
    adapter.setStrategy(strategy);

    // TODO: register before() and after() handler
    /**
    before((request, response) -> {
      T auditor = ((AuditingAdapter<T>) adapter).resolver.resolve(request, response);
      ((AuditingAdapter<T>) adapter).aware.clear();
      ((AuditingAdapter<T>) adapter).aware.update(auditor);
    });

    afterAfter((request, response) -> {
      ((AuditingAdapter<T>) adapter).aware.clear();
    });
    */
  }

  @SuppressWarnings("unchecked")
  public static <T> AuditorAware<T> currentAuditor() {
    return (AuditorAware<T>) adapter.aware;
  }

  public static AuditingHandler handler() {
    return adapter.handler;
  }

  private static class AuditingAdapter<T> {
    private AuditorAwareStrategy<T> aware;
    private final AuditingHandler handler;
    private final AuditorResolver<T> resolver;

    private AuditingAdapter(AuditingHandler handler, AuditorResolver<T> resolver) {
      this.handler = handler;
      this.resolver = resolver;
    }

    private void setStrategy(Strategy strategy) {
      if (strategy == Strategy.INHERITED_THREAD_LOCAL) {
        aware = new InheritableThreadLocalStrategy();
      } else if (strategy == Strategy.GLOBAL) {
        aware = new GlobalStrategy();
      } else if (strategy == Strategy.THREAD_LOCAL) {
        aware = new ThreadLocalStrategy();
      } else {
        LOG.warn("No auditing strategy.");
      }
    }
  }

  static interface AuditorAwareStrategy<T> extends AuditorAware<T> {
    void clear();
    Optional<T> get();
    void update(T auditor);

    Optional<T> getCurrentAuditor();
  }

  static class InheritableThreadLocalStrategy<T> implements AuditorAwareStrategy<T> {
    private final ThreadLocal<T> holder = new InheritableThreadLocal<>();

    @Override
    public void clear() {
      holder.remove();
    }

    @Override
    public void update(T auditor) {
      holder.set(auditor);
    }

    @Override
    public Optional<T> get() {
      return Optional.ofNullable(holder.get());
    }

    @Override
    public Optional<T> getCurrentAuditor() {
      return get();
    }
  }

  static class ThreadLocalStrategy<T> implements AuditorAwareStrategy<T> {
    private final ThreadLocal<T> holder = new ThreadLocal<>();

    @Override
    public void clear() {
      holder.remove();
    }

    @Override
    public void update(T auditor) {
      holder.set(auditor);
    }

    @Override
    public Optional<T> get() {
      return Optional.ofNullable(holder.get());
    }

    @Override
    public Optional<T> getCurrentAuditor() {
      return get();
    }
  }

  static class GlobalStrategy<T> implements AuditorAwareStrategy<T> {
    private T holder;

    @Override
    public void clear() {
      holder = null;
    }

    @Override
    public void update(T auditor) {
      holder = auditor;
    }

    @Override
    public Optional<T> get() {
      return Optional.ofNullable(holder);
    }

    @Override
    public Optional<T> getCurrentAuditor() {
      return get();
    }
  }
}
