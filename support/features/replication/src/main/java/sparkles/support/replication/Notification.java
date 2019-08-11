package sparkles.support.replication;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent=true)
@Data
public class Notification {
  public String url;
}
