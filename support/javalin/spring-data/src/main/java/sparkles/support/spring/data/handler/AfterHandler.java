package sparkles.support.spring.data.handler;

import javax.persistence.EntityManager;

import io.javalin.Context;
import io.javalin.Handler;
import sparkles.support.spring.data.Auditing;

import static sparkles.support.spring.data.SpringDataSupport.closeEntityManager;

public class AfterHandler implements Handler {

  @Override
  public void handle(Context ctx) throws Exception {
    EntityManager entityManager = ctx.attribute(Auditing.CTX_ENTITY_MANAGER);

    // close transaction
    entityManager.getTransaction().commit();
    closeEntityManager(entityManager);
  }

}
