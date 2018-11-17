package sparkles.support.javalin.spring.data.handler;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import io.javalin.Context;
import io.javalin.Handler;
import sparkles.support.javalin.spring.data.SpringDataExtension;

import static sparkles.support.javalin.spring.data.SpringDataExtension.springData;

public class EntityBeforeHandler implements Handler {

  @Override
  public void handle(Context ctx) throws Exception {
    // EntityManager and the factory are request scoped
    EntityManagerFactory entityManagerFactory = ctx.appAttribute(EntityManagerFactory.class);
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);

    springData(ctx).set(entityManager, jpaRepositoryFactory);

    // automatically begin a transaction for a request
    entityManager.getTransaction().begin();
  }

}
