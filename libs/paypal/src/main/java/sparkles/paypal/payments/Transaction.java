package sparkles.paypal.payments;

import com.squareup.moshi.Json;
import lombok.experimental.Accessors;
import lombok.Data;

@Accessors(fluent  = true)
@Data
public class Transaction {

  @Json(name = "reference_id")
  private String referenceId;

  @Json(name = "amount")
  private Amount amount;

  @Json(name = "payee")
  private Payee payee;

  @Json(name = "description")
  private String description;

  @Json(name = "note_to_payee")
  private String noteToPayee;

  @Json(name = "custom")
  private String custom;

  @Json(name = "invoice_number")
  private String invoiceNumber;

  @Json(name = "purchase_order")
  private String purchaseOrder;

  @Json(name = "soft_descriptor")
  private String softDescriptor;

  /** The payment options for this transaction. */
  @Json(name = "payment_options")
  private PaymentOptions paymentOptions;

  /** An array of items that are being purchased. */
  @Json(name = "item_list")
  private ItemList itemList;

  @Json(name = "notify_url")
  private String notifyUrl;

  @Json(name = "order_url")
  private String orderUrl;

}
