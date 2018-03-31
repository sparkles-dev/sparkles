package sparkles.support.persistence;

import spark.Request;
import spark.Response;

/**
 * Maps a request, response-pair to the current auditor.
 */
@FunctionalInterface
public interface AuditorResolver<Auditor> {

  Auditor resolve(Request request, Response response);

}
