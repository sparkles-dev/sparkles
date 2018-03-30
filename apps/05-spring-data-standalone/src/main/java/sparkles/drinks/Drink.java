package sparkles.drinks;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Drink {

  @Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
  )
	@Column(name = "id", updatable = false, nullable = false)
  public UUID id;

  public String name;

  public Drink withName(String name) {
    this.name = name;

    return this;
  }
}
