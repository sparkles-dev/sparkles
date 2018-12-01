package sparkles.support.replication;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Accessors(fluent=true)
@Data
public class Subscription {
  public UUID id;
  public String topic;
  public String notifyUrl;
  public String subscriptionUrl;
}
