package sparkles.hydor.backends.pegelonline;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.Response;

public interface StationsApi {

  @GET("stations.json")
  Call<List<Station>> listStations();

  @GET("stations.json")
  Call<List<Station>> listStationsByWater(@Query("waters") String waters);

}
