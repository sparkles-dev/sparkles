package sparkles.replica.changes;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeRepository extends JpaRepository<ChangeEntity, UUID> {
}
