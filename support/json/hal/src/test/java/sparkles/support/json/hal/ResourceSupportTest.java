package sparkles.support.json.hal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.Test;

import sparkles.support.json.hal.annotation.Embedded;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceSupportTest {

  private final ObjectMapper om = new ObjectMapper()
    .registerModule(new JacksonHalModule());

  @Test
  public void itShouldSerializeAndDeserialize() throws Exception {
    om.addMixIn(Something.class, SomethingMixin.class);
    final SomethingResource input = new SomethingResource();
    input.withDomain(new Something());

    final String jsonString = om.writeValueAsString(input);
    assertThat(jsonString).isEmpty();
    //assertThat(jsonString).doesNotContain("\"time\"");

    final SomethingResource out = om.readValue(jsonString, SomethingResource.class);
    assertThat(out.getSelf().getHref()).isEqualTo(input.getSelf().getHref());
  }

  //@Entity
  public static class Something {

    public UUID id = UUID.randomUUID();

    public Object time = null;

    //@Embedded
    @JsonIgnore
    public Dimensions dimensions = new Dimensions();
  }

  public static class Dimensions {
    public int width = 12;
    public int depth = 20;
    //public LocalDateTime time = LocalDateTime.now();
  }

  public static class SomethingMixin {
    @JsonIgnore
    public LocalDateTime time;
  }

  public static class SomethingResource extends ResourceSupport<Something> {

    @Embedded("dimensions")
    public Dimensions dimensions = new Dimensions();

    public Link getSelf() {
      return new Link.Builder()
        .slash("foo")
        .slash(getDomain().id)
        .build();
    }
  }

}
