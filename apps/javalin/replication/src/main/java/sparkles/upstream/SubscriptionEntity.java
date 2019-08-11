package sparkles.upstream;

import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SubscriptionEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
    name = "UUID",
    strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(name = "ID", updatable = false, nullable = false)
  public UUID id;

  @Column(name = "NOTIFY_URL")
  public String notifyUrl;

  @Column(name = "TOPIC")
  public String topic;

}
