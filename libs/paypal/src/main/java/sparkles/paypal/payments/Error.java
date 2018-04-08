import com.squareup.moshi.Json;
import lombok.experimental.Accessors;
import lombok.Data;

@Accessors(fluent  = true)
@Data
public class Error {

  @Json(name = "error")
  private String error;

  @Json(name = "error_description")
  private String errorDescription;

  @Json(name = "debug_id")
  private String debugId;

  @Json(name = "message")
  private String message;

  @Json(name = "information_link")
  private String informationLink;

}
