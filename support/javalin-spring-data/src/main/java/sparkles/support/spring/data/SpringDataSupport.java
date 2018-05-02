package sparkles.support.spring.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.data.repository.Repository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import io.javalin.Context;

public final class SpringDataSupport {
  private EntityManagerFactory entityManagerFactory;

  void init(EntityManagerFactory entityManagerFactory) {
    // EntityManagerFactory is application-scoped
    this.entityManagerFactory = entityManagerFactory;

    // TODO: register before() and after() handler
  }

  void shutdown() {
    closeEntityManagerFactory(entityManagerFactory);
  }

  public static void initPersistence(EntityManagerFactory entityManagerFactory) {
    getInstance().init(entityManagerFactory);
  }

  public static void shutdownPersistence() {
    getInstance().shutdown();
  }

  public static void closeEntityManagerFactory(EntityManagerFactory factory) {
    factory.close();
  }

  public static EntityManager createEntityManager(EntityManagerFactory factory) {
    return factory.createEntityManager();
  }

  public static void closeEntityManager(EntityManager entityManager) {
    entityManager.close();
  }

  public static JpaRepositoryFactory createJpaRepositoryFactory(EntityManager entityManager) {
    return new JpaRepositoryFactory(entityManager);
  }

  public static JpaRepositoryFactory jpaRepositoryFactory(Context ctx) {
    return ctx.attribute("persistence.jpaRepositoryFactory");
  }

  public static <T extends Repository> T repository(Context ctx, Class<T> repositoryClazz) {
    return jpaRepositoryFactory(ctx).getRepository(repositoryClazz);
  }

  private SpringDataSupport() {}

  private static class SingletonHolder {
    private static final SpringDataSupport INSTANCE = new SpringDataSupport();
  }

  private static SpringDataSupport getInstance() {
    return SingletonHolder.INSTANCE;
  }
}
