package sparkles.support.spring.data;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.AuditorAware;
import org.springframework.util.Assert;

/**
 * Adapter for connecting Spring's auditing infrastructure to Hibernate in standalone usage.
 *
 * Since most of the related Spring APIs are either exernal or hidden behind AspectJ, they are
 * re-implemented here for feature parity.
 *
 * @link https://github.com/spring-projects/spring-data-jpa/blob/master/src/main/java/org/springframework/data/jpa/domain/support/AuditingEntityListener.java
 * @link https://github.com/spring-projects/spring-data-commons/blob/master/src/main/java/org/springframework/data/auditing/AuditingHandler.java#L160-L176
 * @link https://github.com/spring-projects/spring-data-commons/blob/master/src/main/java/org/springframework/data/auditing/DefaultAuditableBeanWrapperFactory.java#L57-L75
 * @link https://github.com/spring-projects/spring-data-commons/blob/master/src/main/java/org/springframework/data/auditing/AnnotationAuditingMetadata.java#L38-L47
 */
public class AuditingEntityListener {
  private static final Logger LOG = LoggerFactory.getLogger(AuditorAware.class);

  @PrePersist
  public void touchForCreate(Object target) {
    Assert.notNull(target, "Entity must not be null!");

    AuditingHandler handler = Auditing.handler();
    if (handler != null) {
      handler.setAuditorAware(Auditing.currentAuditor());
      handler.markCreated(target);
    }
  }

  @PreUpdate
  public void touchForUpdate(Object target) {
    Assert.notNull(target, "Entity must not be null!");

    AuditingHandler handler = Auditing.handler();
    if (handler != null) {
      handler.setAuditorAware(Auditing.currentAuditor());
      handler.markModified(target);
    }
  }

}
