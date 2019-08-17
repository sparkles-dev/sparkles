package sparkles.replica.collection;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CollectionEntity {

  @Id
  public UUID id = UUID.randomUUID();

  public String name;
}
