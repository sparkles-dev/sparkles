package sparkles.support.javalin.spring.data.handler;

import javax.persistence.EntityManager;

import io.javalin.Context;
import io.javalin.Handler;

import static sparkles.support.javalin.spring.data.SpringDataExtension.springData;

public class EntityAfterHandler implements Handler {

  @Override
  public void handle(Context ctx) throws Exception {
    EntityManager entityManager = springData(ctx).entityManager();

    // close transaction
    entityManager.getTransaction().commit();
    entityManager.close();
  }

}
