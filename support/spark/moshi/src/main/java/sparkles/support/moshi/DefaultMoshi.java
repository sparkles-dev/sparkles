package sparkles.support.moshi;

import com.squareup.moshi.Moshi;
import sparkles.support.moshi.adapter.LocalDateTimeAdapter;
import sparkles.support.moshi.adapter.UuidAdapter;
import sparkles.support.moshi.adapter.ZonedDateTimeAdapter;

public final class DefaultMoshi {
  private static final Moshi DEFAULT = new Moshi.Builder()
    .add(LocalDateTimeAdapter.TYPE, new LocalDateTimeAdapter())
    .add(UuidAdapter.TYPE, new UuidAdapter())
    .add(ZonedDateTimeAdapter.TYPE, new ZonedDateTimeAdapter())
    .build();

  public static Moshi defaultMoshi() {
    return DEFAULT;
  }

  public static Moshi.Builder newMoshi() {
    return DEFAULT.newBuilder();
  }

}
