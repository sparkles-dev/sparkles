package sparkles.support.moshi.adapter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.util.UUID;

public class UuidAdapter extends JsonAdapter<UUID> {
  public static final Class<?> TYPE = UUID.class;

  @Override
  public UUID fromJson(JsonReader jsonReader) throws IOException {
    return UUID.fromString(jsonReader.nextString());
  }

  @Override
  public void toJson(JsonWriter jsonWriter, UUID uuid) throws IOException {
    jsonWriter.value(uuid.toString());
  }

}
