package sparkles.support.spring.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mapping.context.PersistentEntities;

import java.util.Collections;
import java.util.Optional;

public class AuditingContext<T> implements AuditorAware<T> {
  private static final Logger LOG = LoggerFactory.getLogger(AuditingContext.class);

  private AuditingHandler handler;
  private T currentAuditor;

  protected AuditingContext() {
    this(new AuditingHandler(new PersistentEntities(Collections.emptyList())));
  }

  protected AuditingContext(AuditingHandler handler) {
    this.handler = handler;
  }

  public AuditingHandler getHandler() {
    return handler;
  }

  public void setHandler(AuditingHandler handler) {
    this.handler = handler;
  }

  @Override
  public Optional<T> getCurrentAuditor() {
    return Optional.ofNullable(currentAuditor);
  }

  public void clearCurrentAuditor() {
    currentAuditor = null;
  }

  public void setCurrentAuditor(T currentAuditor) {
    this.currentAuditor = currentAuditor;
  }
}
