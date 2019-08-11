package sparkles.relation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RelationRepository extends JpaRepository<RelationEntity, UUID> {
}
