package sparkles.support.spring.data.handler;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import io.javalin.Context;
import io.javalin.Handler;

import static sparkles.support.spring.data.SpringDataSupport.createEntityManager;
import static sparkles.support.spring.data.SpringDataSupport.createJpaRepositoryFactory;

public class BeforeHandler implements Handler {
  private final EntityManagerFactory entityManagerFactory;

  public BeforeHandler(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public void handle(Context ctx) throws Exception {
    // EntityManager and the factory are request scoped
    EntityManager entityManager = createEntityManager(entityManagerFactory);
    JpaRepositoryFactory jpaRepositoryFactory = createJpaRepositoryFactory(entityManager);

    ctx.attribute("persistence.entityManager", entityManager);
    ctx.attribute("persistence.jpaRepositoryFactory", jpaRepositoryFactory);

    // automatically begin a transaction for a request
    entityManager.getTransaction().begin();
  }

}
