package sparkles.replica.collection;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CollectionRepository extends JpaRepository<CollectionEntity, UUID> {

  boolean existsByName(String name);

  Optional<CollectionEntity> findByName(String name);
}
