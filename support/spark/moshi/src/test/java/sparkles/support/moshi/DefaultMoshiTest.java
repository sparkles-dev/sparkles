package sparkles.support.moshi;

import com.squareup.moshi.JsonAdapter;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static sparkles.support.moshi.DefaultMoshi.defaultMoshi;

public class DefaultMoshiTest {

  static class MockWithUUID {
    UUID value = UUID.fromString("2b14ec17-84e1-4d28-b945-ff2e975e0dff");
  }

  static class MockWithLocalDateTime {
    LocalDateTime value = LocalDateTime.parse("2015-08-04T10:11:30");
  }

  static class MockWithZonedDateTime {
    ZonedDateTime value = ZonedDateTime.parse("2007-12-03T10:15:30+01:00");
  }

  @Test
  public void itConvertsUUID() {
    JsonAdapter<MockWithUUID> adapter = defaultMoshi().adapter(MockWithUUID.class);

    String parsed = adapter.toJson(new MockWithUUID());

    assertThat(parsed).isEqualTo("{\"value\":\"2b14ec17-84e1-4d28-b945-ff2e975e0dff\"}");
  }

  @Test
  public void itConvertsLocalDateTime() {
    JsonAdapter<MockWithLocalDateTime> adapter = defaultMoshi().adapter(MockWithLocalDateTime.class);

    String parsed = adapter.toJson(new MockWithLocalDateTime());

    assertThat(parsed).isEqualTo("{\"value\":\"2015-08-04T10:11:30\"}");
  }

  @Test
  public void itConvertsZonedDateTime() {
    JsonAdapter<MockWithZonedDateTime> adapter = defaultMoshi().adapter(MockWithZonedDateTime.class);

    String parsed = adapter.toJson(new MockWithZonedDateTime());

    assertThat(parsed).isEqualTo("{\"value\":\"2007-12-03T10:15:30+01:00\"}");
  }

}
