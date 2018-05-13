package sparkles.support.spring.data.handler;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import io.javalin.Context;
import io.javalin.Handler;
import sparkles.support.spring.data.SpringDataExtension;

public class EntityBeforeHandler implements Handler {
  private final EntityManagerFactory entityManagerFactory;

  public EntityBeforeHandler(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public void handle(Context ctx) throws Exception {
    // EntityManager and the factory are request scoped
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);

    ctx.attribute(SpringDataExtension.CTX_ENTITY_MANAGER, entityManager);
    ctx.attribute(SpringDataExtension.CTX_JPA_REPOSITORY_FACTORY, jpaRepositoryFactory);

    // automatically begin a transaction for a request
    entityManager.getTransaction().begin();
  }

}
