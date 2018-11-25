package sparkles.support.javalin.testing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class HttpClient {

  public final OkHttpClient okHttp;
  private final Request requestTemplate;

  private Request lastRequest;
  private Call lastCall;
  private Response lastResponse;

  private String _url;
  private String _method;
  private RequestBody _body;
  private Map<String, String> _headers = new HashMap<>();

  public HttpClient(OkHttpClient client, Request requestTemplate) {
    this.okHttp = client;
    this.requestTemplate = requestTemplate;
  }

  public HttpClient get(String url) {
    return this.request("GET", url);
  }

  public HttpClient post(String url) {
    return this.request("POST", url);
  }

  // TODO: add more http verbs

  public HttpClient request(String method, String url) {
    this._url = url;
    this._method = method;
    this._body = null;
    this._headers.clear();
    // TODO: clear the other states

    return this;
  }

  public HttpClient json(String jsonString) {
    return body(RequestBody.create(MediaType.parse("application/json"), jsonString));
  }

  public HttpClient body(RequestBody body) {
    this._body = body;

    return this;
  }

  public HttpClient emptyBody() {
    this._body = RequestBody.create(MediaType.parse("text/plain"), "");

    return this;
  }

  public HttpClient header(String header, String value) {
    _headers.put(header, value);

    return this;
  }

  public Response send() {
    if (_url.startsWith("/")) {
      _url = _url.substring(1, _url.length());
    }

    try {
      lastRequest = requestTemplate.newBuilder()
        .method(_method, _body)
        .url(requestTemplate.url()
          .newBuilder()
          .addPathSegment(_url)
          .build())
        .headers(Headers.of(_headers))
        .build();
      lastCall = okHttp.newCall(lastRequest);
      lastResponse = lastCall.execute();

      return lastResponse;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Call lastCall() {
    return lastCall;
  }

  public Request lastRequest() {
    return lastRequest;
  }

  public Response lastResponse() {
    return lastResponse;
  }

  public void release() {
    okHttp.dispatcher().executorService().shutdown();
    okHttp.connectionPool().evictAll();
  }

}
