package sparkles.support.spring.data;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import io.javalin.Context;
import io.javalin.event.EventType;
import sparkles.support.javalin.Extension;
import sparkles.support.javalin.JavalinApp;
import sparkles.support.spring.data.handler.EntityAfterHandler;
import sparkles.support.spring.data.handler.EntityBeforeHandler;

public class SpringDataExtension implements Extension {

  public static final String CTX_ENTITY_MANAGER = "persistence.entityManager";
  public static final String CTX_JPA_REPOSITORY_FACTORY = "persistence.jpaRepositoryFactory";

  private final EntityManagerFactory entityManagerFactory;

  public SpringDataExtension(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public void register(JavalinApp app) {

    app.before(new EntityBeforeHandler(entityManagerFactory))
      .after(new EntityAfterHandler())
      .event(EventType.SERVER_STOPPING, (evt) -> {
        entityManagerFactory.close();
      });

  }

  public static EntityManager entityManager(Context ctx) {
    return ctx.attribute(CTX_ENTITY_MANAGER);
  }

  public static JpaRepositoryFactory jpaRepositoryFactory(Context ctx) {
    return ctx.attribute(CTX_JPA_REPOSITORY_FACTORY);
  }

  public static <T> T createRepository(Context ctx, Class<T> repositoryClazz) {
    return jpaRepositoryFactory(ctx).getRepository(repositoryClazz);
  }
}
