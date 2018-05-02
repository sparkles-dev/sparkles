package sparkles.support.spring.data.handler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import io.javalin.Context;
import io.javalin.Handler;

import static sparkles.support.spring.data.SpringDataSupport.closeEntityManager;

public class AfterHandler implements Handler {

  @Override
  public void handle(Context ctx) throws Exception {
    EntityManager entityManager = ctx.attribute("persistence.entityManager");

    // close transaction
    entityManager.getTransaction().commit();
    closeEntityManager(entityManager);
  }

}
