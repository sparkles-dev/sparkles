package sparkles.support.moshi;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.JsonAdapter;
import java.util.Arrays;
import java.util.List;
import spark.ResponseTransformer;

public class MoshiResponseTransformer<T> implements ResponseTransformer  {

  private final Moshi moshi;
  private final List<Class<?>> types;

  private MoshiResponseTransformer(Moshi moshi, List<Class<?>> types) {
    this.moshi = moshi;
    this.types = types;
  }

  @Override
  public String render(Object model) {
    final JsonAdapter jsonAdapter = this.types.stream()
      .filter(type -> model.getClass().isAssignableFrom(type))
      .findFirst()
      .map(type -> moshi.adapter(type))
      .orElseThrow(() -> new RuntimeException("No adapter registered for " + model.getClass().getCanonicalName()));

    return jsonAdapter.toJson(model);
  }

  public static <T> MoshiResponseTransformer<T> moshiTransformer(Class<T> type) {
    return moshiTransformer(
      new Moshi.Builder().build(),
      type
    );
  }

  public static <T> MoshiResponseTransformer<T> moshiTransformer(Moshi moshi, Class<T> type) {
    return new MoshiResponseTransformer(
      moshi,
      Arrays.asList(type)
    );
  }

  public static MoshiResponseTransformer<?> moshiTransformer(Moshi moshi, Class<?> type, Class<?> type2) {
    return new MoshiResponseTransformer(
      moshi,
      Arrays.asList(type, type2)
    );
  }

}
