package sparkles.hydor.backends.pegelonline;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sparkles.support.okhttp.LoggingInterceptor;

import static org.assertj.core.api.Assertions.assertThat;

public class StationsApiTest {

  private static final Logger log = LoggerFactory.getLogger(StationsApiTest.class);

  final StationsApi api = new Pegelonline.Builder()
    .live()
    .logging(LoggingInterceptor.slf4j(log))
    .build()
    .stationsApi();

  @Test
  public void itShouldCreate() {
    assertThat(api).isNotNull();
  }

  @Test
  public void listStations() throws Exception {
    final List<Station> list = api.listStations().execute().body();

    assertThat(list).isNotEmpty();
    assertThat(list.get(0).uuid).isEqualTo(UUID.fromString("47174d8f-1b8e-4599-8a59-b580dd55bc87"));
  }

  @Test
  public void listStationsByWater() throws Exception {
    final List<Station> list = api.listStationsByWater("mosel").execute().body();

    assertThat(list).isNotEmpty();
    assertThat(list.get(0).uuid).isEqualTo(UUID.fromString("896aed46-8290-49b0-a6f8-90d53027cd1f"));
  }

}
