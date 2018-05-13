package sparkles.support.spring.data.handler;

import javax.persistence.EntityManager;

import io.javalin.Context;
import io.javalin.Handler;
import sparkles.support.spring.data.SpringDataExtension;

public class EntityAfterHandler implements Handler {

  @Override
  public void handle(Context ctx) throws Exception {
    EntityManager entityManager = ctx.attribute(SpringDataExtension.CTX_ENTITY_MANAGER);

    // close transaction
    entityManager.getTransaction().commit();
    entityManager.close();
  }

}
