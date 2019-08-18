package sparkles.replica;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import sparkles.support.common.collections.Collections;
import sparkles.support.json.JavaxJson;

public class R {

  private final Context ctx;

  private R (Context ctx) {
    this.ctx = ctx;
  }

  public static Plugin createPlugin() {
    return new Plugin();
  }

  private static class Plugin implements io.javalin.core.plugin.Plugin {

    @Override
    public void apply(Javalin app) {

      app.before(ctx -> {
        ctx.register(R.class, new R(ctx));
      });

    }

  }

  public static <T extends Exception> ExceptionHandler<T> jsonExceptionResponse(int status) {
    return (e, ctx) -> {
      final JsonObject msg = Json.createObjectBuilder()
        .add("code", status)
        .add("type", e.getClass().getCanonicalName())
        .add("message", e.getMessage())
        .build();

      ctx.status(status);
      ctx.use(R.class).result(msg);
    };
  }

  private JsonStructure entity;

  public JsonStructure body() {
    if (entity == null) {
      entity = JavaxJson.readJson(ctx.body());
    }

    return entity;
  }

  public JsonValue jsonProperty(String pointer) {
    return body().getValue(pointer);
  }

  public <T> T jsonProperty(String pointer, Predicate<JsonValue> test, Function<JsonValue, T> mapper) {
    final JsonValue value = jsonProperty(pointer);

    if (test.test(value)) {
      return mapper.apply(value);
    } else {
      throw new IllegalArgumentException();
    }
  }

  public <T> T jsonProperty(String pointer, JsonValue.ValueType type, Function<JsonValue, T> mapper) {
    return jsonProperty(
      pointer,
      value -> value.getValueType() == type,
      mapper
    );
  }

  public String jsonPropertyString(String pointer) {
    return jsonProperty(pointer, JsonValue.ValueType.STRING, v -> ((JsonString) v).getString());
  }

  public UUID jsonPropertyUuid(String pointer) {
    return UUID.fromString(jsonPropertyString(pointer));
  }

  public void result(JsonStructure json) {
    ctx.header("Content-Type", "application/json");
    ctx.result(json.toString());
  }

}
