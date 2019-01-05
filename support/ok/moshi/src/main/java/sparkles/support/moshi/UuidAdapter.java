package sparkles.support.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.util.UUID;

public class UuidAdapter extends JsonAdapter<UUID> {
  public static final Class<?> TYPE = UUID.class;

  @Override
  public UUID fromJson(JsonReader jsonReader) throws IOException {
    if (jsonReader.peek() != JsonReader.Token.NULL) {
      return UUID.fromString(jsonReader.nextString());
    } else {
      return null;
    }
  }

  @Override
  public void toJson(JsonWriter jsonWriter, UUID uuid) throws IOException {
    if (uuid == null) {
      jsonWriter.nullValue();
    } else {
      jsonWriter.value(uuid.toString());
    }
  }

}
