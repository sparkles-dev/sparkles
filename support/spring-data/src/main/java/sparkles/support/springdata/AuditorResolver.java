package sparkles.support.springdata;

import spark.Request;
import spark.Response;

/**
 * Maps a request, response-pair to the current auditor.
 */
@FunctionalInterface
public interface AuditorResolver<Auditor> {

  Auditor resolve(Request request, Response response);

}
