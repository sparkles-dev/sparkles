package sparkles.paypal.payments;

import java.util.ArrayList;
import java.util.List;
import com.squareup.moshi.Json;
import lombok.experimental.Accessors;
import lombok.Data;

@Accessors(fluent  = true)
@Data
public class ItemList {

  /** An array of items that are being purchased. */
  @Json(name = "items")
  private List<Item> items = new ArrayList<>();

  /** The shipping address details. */
  @Json(name = "shipping_address")
  private ShippingAddress shippingAddress;

  /** The shipping method used for this payment, such as USPS Parcel. */
  @Json(name = "shipping_method")
  private String shippingMethod;

  /** The shipping phone number, in its canonical international format as defined by the E.164 numbering plan. Enables merchants to share payer’s contact number with PayPal for the current payment. The final contact number for the payer who is associated with the transaction might be the same as or different from the shipping_phone_number based on the payer’s action on PayPal. */
  @Json(name = "shipping_phone_number")
  private String shippingPhoneNumber;

}
