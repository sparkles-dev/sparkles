package sparkles.support.json;

import java.io.InputStream;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;

public class JavaxJson {

  public static JsonObject readJsonObject(String value) {
    try (final JsonReader reader = Json.createReader(new StringReader(value))) {
      return reader.readObject();
    }
  }

  public static JsonObject readJsonObject(InputStream value) {
    try (final JsonReader reader = Json.createReader(value)) {
      return reader.readObject();
    }
  }

  public static JsonStructure readJson(String value) {
    try (final JsonReader reader = Json.createReader(new StringReader(value))) {
      return reader.read();
    }
  }

  public static String propertyString(JsonStructure structure, String jsonPointer) {
    final JsonValue value = structure.getValue(jsonPointer);

    if (value.getValueType() == JsonValue.ValueType.STRING) {
      return ((JsonString) value).getString();
    } else {
      throw new IllegalArgumentException();
    }
  }

}
