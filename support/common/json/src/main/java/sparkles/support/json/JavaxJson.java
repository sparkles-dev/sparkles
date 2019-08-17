package sparkles.support.json;

import java.io.InputStream;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

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

}
