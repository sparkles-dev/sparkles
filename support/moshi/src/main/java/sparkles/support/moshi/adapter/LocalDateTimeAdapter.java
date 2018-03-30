package sparkles.support.moshi.adapter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends JsonAdapter<LocalDateTime> {
  public static final Class<?> TYPE = LocalDateTime.class;

  @Override
  public LocalDateTime fromJson(JsonReader jsonReader) throws IOException {

    JsonReader.Token token = jsonReader.peek();
    if (token == JsonReader.Token.STRING) {
      return LocalDateTime.parse(jsonReader.nextString());
    } else {
      jsonReader.skipValue();
      return null;
    }
  }

  @Override
  public void toJson(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
    if (localDateTime != null) {
      jsonWriter.value(localDateTime.toString());
    } else {
      jsonWriter.nullValue();
    }
  }

}
