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

  public static final String CTX_ENTITY_MANAGER = "persistence.entityManager";
  public static final String CTX_JPA_REPOSITORY_FACTORY = "persistence.jpaRepositoryFactory";

  public enum Strategy {
    GLOBAL,
    INHERITED_THREAD_LOCAL,
    THREAD_LOCAL;
  }

  private static AuditingContext<?> context;

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
    context = new AuditingContext(handler, resolver);
    context.setStrategy(strategy);

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
  public static <T> AuditingContext<T> context() {
    return (AuditingContext<T>) context;
  }

  /** @deprecated */
  @SuppressWarnings("unchecked")
  public static <T> AuditorAware<T> currentAuditor() {
    return (AuditorAware<T>) context.getAware();
  }

  /** @deprecated */
  public static AuditingHandler handler() {
    return context.getHandler();
  }

  interface AuditorAwareStrategy<T> extends AuditorAware<T> {
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
