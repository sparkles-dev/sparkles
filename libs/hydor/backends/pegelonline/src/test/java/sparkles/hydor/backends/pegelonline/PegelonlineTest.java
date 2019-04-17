package sparkles.hydor.backends.pegelonline;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PegelonlineTest {

  @Test
  public void itShouldCreate() {
    final Pegelonline po = new Pegelonline.Builder()
      .live()
      .build();
    assertThat(po).isNotNull();
  }

  @Test
  public void itShouldCreateStationsApi() {
    final StationsApi api = new Pegelonline.Builder()
      .live()
      .build()
      .stationsApi();
    assertThat(api).isNotNull();
  }

}
