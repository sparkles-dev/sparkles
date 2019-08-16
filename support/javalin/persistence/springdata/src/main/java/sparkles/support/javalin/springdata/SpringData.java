package sparkles.support.javalin.springdata;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.Repository;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true)
public class SpringData {
  private final EntityManager entityManager;
  private final JpaRepositoryFactory jpaRepositoryFactory;

  public <T extends Repository> T repository(Class<T> repoClz) {
    return jpaRepositoryFactory.getRepository(repoClz);
  }
}
