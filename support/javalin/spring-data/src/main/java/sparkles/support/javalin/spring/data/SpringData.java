package sparkles.support.javalin.spring.data;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.Repository;

import javax.persistence.EntityManager;

public class SpringData {
  private final EntityManager entityManager;
  private final JpaRepositoryFactory jpaRepositoryFactory;

  public SpringData(EntityManager entityManager, JpaRepositoryFactory jpaRepositoryFactory) {
    this.entityManager = entityManager;
    this.jpaRepositoryFactory = jpaRepositoryFactory;
  }

  public EntityManager entityManager() {
    return entityManager;
  }

  public JpaRepositoryFactory jpaRepositoryFactory() {
    return jpaRepositoryFactory;
  }

  public <T extends Repository> T repository(Class<T> repoClz) {
    return jpaRepositoryFactory().getRepository(repoClz);
  }
}
