package sparkles.support.javalin.http;

import java.util.Collections;

import io.javalin.http.HttpResponseException;

public class PreconditionFailedResponse extends HttpResponseException {
  private static final long serialVersionUID = 1547829376953051943L;

  public PreconditionFailedResponse() {
    super(412, "Precondition Failed", Collections.emptyMap());
	}
}
