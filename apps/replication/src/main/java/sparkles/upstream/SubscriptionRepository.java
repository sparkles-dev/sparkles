package sparkles.upstream;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {

  List<SubscriptionEntity> findByTopic(String topic);
}
