package sparkles.support.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import spark.Request;
import spark.Response;
import spark.Route;

import static sparkles.support.moshi.DefaultMoshi.newMoshi;

@FunctionalInterface
public interface MoshiRoute<T, R> {

  R handle(Request req, Response res, T entity) throws Exception;

  public static<T, R> Route moshiRoute(MoshiRoute<T, R> route, Class<T> type) {
    return moshiRoute(route, type, newMoshi().build());
  }

  public static<T, R> Route moshiRoute(MoshiRoute<T, R> route, Class<T> type, Moshi moshi) {
    final JsonAdapter<T> adapter = moshi.adapter(type);

    return (request, response) -> {
      T entity = adapter.fromJson(request.body());

      return route.handle(request, response, entity);
    };
  }
}
