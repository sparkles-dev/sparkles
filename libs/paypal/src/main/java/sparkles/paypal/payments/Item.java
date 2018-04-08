package sparkles.paypal.payments;

import java.util.List;
import com.squareup.moshi.Json;
import lombok.experimental.Accessors;
import lombok.Data;

@Accessors(fluent  = true)
@Data
public class Item {

  /** The stock keeping unit (SKU) for the item. */
  @Json(name = "sku")
  private String sku;

  @Json(name = "name")
  private String name;

  @Json(name = "description")
  private String description;

  /** Required. The item quantity. Must be a whole number. */
  @Json(name = "quantity")
  private String quantity;

  /** Required. The item cost. Supports two decimal places. */
  @Json(name = "price")
  private String price;

  /** Required. The three-character ISO-4217 currency code. */
  @Json(name = "currency")
  private String currency;

}
