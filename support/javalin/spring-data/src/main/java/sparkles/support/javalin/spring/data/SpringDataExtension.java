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
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
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
        ctx.register(SpringData.class, new SpringData(new SpringDataContext(entityManager, jpaRepositoryFactory)));

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

  public static SpringData springData(Context ctx) {
    return new SpringData(new SpringDataContext(ctx.use(EntityManager.class), ctx.use(JpaRepositoryFactory.class)));
  }

  @Data
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  @Accessors(fluent = true)
  public static class SpringDataContext {

    private final Map<Class, Object> repos = new HashMap<>();

    /**
     * Returns the EntityManager for the request context
     */
    private final EntityManager entityManager;

    /**
     * Returns the JpaRepositoryFactory for the request context
     */
    private final JpaRepositoryFactory jpaRepositoryFactory;

    @SuppressWarnings("unchecked")
    public <T> T createRepository(Class<T> repositoryClazz) {
      return repository(repositoryClazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T repository(Class<T> repositoryClz) {
      if (repos.containsKey(repositoryClz)) {
        return (T) repos.get(repositoryClz);
      } else {
        T repo = jpaRepositoryFactory.getRepository(repositoryClz);
        repos.put(repositoryClz, repo);

        return repo;
      }
    }

  }

}
