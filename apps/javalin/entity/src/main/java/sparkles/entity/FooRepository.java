package sparkles.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FooRepository extends JpaRepository<FooEntity, UUID> {
}
