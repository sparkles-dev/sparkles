package sparkles.drinks;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrinkRepository extends JpaRepository<Drink, UUID> {

}
