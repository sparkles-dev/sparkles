package sparkles.support.javalin.springdata.auditing;

import io.javalin.Context;

/**
 * Maps a request, response-pair to the current auditor.
 */
@FunctionalInterface
public interface AuditorResolver<Auditor> {

  Auditor resolve(Context ctx);

}
