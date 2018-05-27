package sparkles;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StuffRepository extends JpaRepository<StuffEntity, UUID> {
}
