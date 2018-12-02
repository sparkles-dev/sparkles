package sparkles.support.javalin.spring.data;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.Repository;

import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import io.javalin.Context;
import io.javalin.JavalinEvent;
import sparkles.support.javalin.Extension;
import sparkles.support.javalin.JavalinApp;
import sparkles.support.javalin.spring.data.handler.EntityAfterHandler;
import sparkles.support.javalin.spring.data.handler.EntityBeforeHandler;

public class SpringDataExtension implements Extension {

  private final Supplier<EntityManagerFactory> entityManagerFactory;

  private SpringDataExtension(Supplier<EntityManagerFactory> entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public void addToJavalin(JavalinApp app) {

    app.event(JavalinEvent.SERVER_STARTING, () -> {
        app.attribute(EntityManagerFactory.class, entityManagerFactory.get());
      })
      .before(new EntityBeforeHandler())
      .after(new EntityAfterHandler())
      .event(JavalinEvent.SERVER_STOPPING, () -> {
        app.attribute(EntityManagerFactory.class).close();
      });

  }

  public static SpringDataExtension create(Supplier<EntityManagerFactory> entityManagerFactory) {
    return new SpringDataExtension(entityManagerFactory);
  }

  public static SpringDataExtensionContext springData(Context ctx) {
    return new SpringDataExtensionContext(ctx);
  }

  public static class SpringDataExtensionContext {
    private static final String CTX_ENTITY_MANAGER = "persistence.entityManager";
    private static final String CTX_JPA_REPOSITORY_FACTORY = "persistence.jpaRepositoryFactory";

    private final Context ctx;

    private SpringDataExtensionContext(Context ctx) {
      this.ctx = ctx;
    }

    public EntityManager entityManager() {
      return this.ctx.attribute(CTX_ENTITY_MANAGER);
    }

    public JpaRepositoryFactory jpaRepositoryFactory() {
      return this.ctx.attribute(CTX_JPA_REPOSITORY_FACTORY);
    }

    public <T> T createRepository(Class<T> repositoryClazz) {
      return this.jpaRepositoryFactory().getRepository(repositoryClazz);
    }

    public SpringDataExtensionContext set(EntityManager entityManager, JpaRepositoryFactory jpaRepositoryFactory) {
      this.ctx.attribute(CTX_ENTITY_MANAGER, entityManager);
      this.ctx.attribute(CTX_JPA_REPOSITORY_FACTORY, jpaRepositoryFactory);

      return this;
    }
  }

}
