package sparkles.support.moshi.adapter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.time.ZonedDateTime;

public class ZonedDateTimeAdapter extends JsonAdapter<ZonedDateTime> {
  public static final Class<?> TYPE = ZonedDateTime.class;

  @Override
  public ZonedDateTime fromJson(JsonReader jsonReader) throws IOException {
    JsonReader.Token token = jsonReader.peek();
    if (token == JsonReader.Token.STRING) {
      return ZonedDateTime.parse(jsonReader.nextString());
    } else {
      jsonReader.skipValue();
      return null;
    }
  }

  @Override
  public void toJson(JsonWriter jsonWriter, ZonedDateTime value) throws IOException {
    if (value != null) {
      jsonWriter.value(value.toString());
    } else {
      jsonWriter.nullValue();
    }
  }

}
