package sparkles.support.javalin.springdata;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.Repository;

import java.util.Map;
import java.util.HashMap;
import javax.persistence.EntityManager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true)
public class SpringData {
  @Getter
  private final EntityManager entityManager;

  @Getter
  private final JpaRepositoryFactory jpaRepositoryFactory;

  private final Map<Class, Object> repos = new HashMap<>();

  public <T extends Repository> T repository(Class<T> repoClz) {
    return jpaRepositoryFactory.getRepository(repoClz);
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
