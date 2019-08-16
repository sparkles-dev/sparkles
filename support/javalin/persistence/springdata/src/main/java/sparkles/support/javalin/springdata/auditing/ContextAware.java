package sparkles.support.javalin.springdata.auditing;

import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mapping.context.PersistentEntities;

import java.util.Collections;
import java.util.Optional;

public class ContextAware<T> implements AuditorAware<T> {

  private AuditingHandler handler;
  private T currentAuditor;

  protected ContextAware() {
    this(new AuditingHandler(new PersistentEntities(Collections.emptyList())));
  }

  protected ContextAware(AuditingHandler handler) {
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
