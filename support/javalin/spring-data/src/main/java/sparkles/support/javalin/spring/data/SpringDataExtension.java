package sparkles.support.javalin.spring.data;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import io.javalin.Context;
import io.javalin.JavalinEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import sparkles.support.javalin.Extension;
import sparkles.support.javalin.JavalinApp;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringDataExtension implements Extension {

  private final Supplier<EntityManagerFactory> entityManagerFactory;

  @Override
  public void addToJavalin(JavalinApp app) {

    app.event(JavalinEvent.SERVER_STARTING, () -> {
        app.attribute(EntityManagerFactory.class, entityManagerFactory.get());
      })
      .before(ctx -> {
        // EntityManagerFactory is application scoped
        EntityManagerFactory entityManagerFactory = ctx.appAttribute(EntityManagerFactory.class);
        // EntityManager and JpaRepositoryFactory are request scoped
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);

        ctx.register(EntityManager.class, entityManager);
        ctx.register(JpaRepositoryFactory.class, jpaRepositoryFactory);
        ctx.register(SpringData.class, new SpringData(entityManager, jpaRepositoryFactory));

        // Begin a transaction per request (transaction boundary is request boundary)
        entityManager.getTransaction().begin();

      })
      .after(ctx -> {
        EntityManager entityManager = springData(ctx).entityManager();

        // Commit and close transaction
        entityManager.getTransaction().commit();
        entityManager.close();
      })
      .event(JavalinEvent.SERVER_STOPPING, () -> {
        app.attribute(EntityManagerFactory.class).close();
      });

  }

  public static SpringDataExtension create(Supplier<EntityManagerFactory> entityManagerFactory) {
    return new SpringDataExtension(entityManagerFactory);
  }

  public static SpringDataContext springData(Context ctx) {
    return new SpringDataContext(ctx);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class SpringDataContext {
    private final Context ctx;
    private final Map<Class, Object> repos = new HashMap<>();

    /**
     * Returns the EntityManager for the request context
     * @return EntityManager
     */
    public EntityManager entityManager() {
      return ctx.use(EntityManager.class);
    }

    /**
     * Returns the JpaRepositoryFactory the request context
     * @return
     */
    public JpaRepositoryFactory jpaRepositoryFactory() {
      return ctx.use(JpaRepositoryFactory.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T createRepository(Class<T> repositoryClazz) {
      if (repos.containsKey(repositoryClazz)) {
        return (T) repos.get(repositoryClazz);
      } else {
        T repo = jpaRepositoryFactory().getRepository(repositoryClazz);
        repos.put(repositoryClazz, repo);

        return repo;
      }
    }
  }

}
