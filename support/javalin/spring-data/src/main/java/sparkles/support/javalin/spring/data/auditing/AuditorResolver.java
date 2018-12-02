package sparkles.support.javalin.spring.data.auditing;

import io.javalin.Context;

/**
 * Maps a request, response-pair to the current auditor.
 */
@FunctionalInterface
public interface AuditorResolver<Auditor> {

  Auditor resolve(Context ctx);

}
