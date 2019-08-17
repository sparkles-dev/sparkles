package sparkles.support.javalin.testing;

import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import lombok.extern.slf4j.Slf4j;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import sparkles.support.moshi.LocalDateTimeAdapter;
import sparkles.support.moshi.UuidAdapter;
import sparkles.support.moshi.ZonedDateTimeAdapter;

@Slf4j
public final class HttpClient {

  private final Request requestTemplate;
  private OkHttpClient okHttp;

  private Request lastRequest;
  private Call lastCall;
  private Response lastResponse;
  private ResponseBody lastResponseBody;

  private String reqUrl;
  private String reqMethod;
  private RequestBody reqBody;
  private Map<String, String> reqHeaders = new HashMap<>();

  public HttpClient(OkHttpClient client, Request requestTemplate) {
    this.okHttp = client;
    this.requestTemplate = requestTemplate;
    enableLogging(HttpLoggingInterceptor.Level.BASIC);
  }

  public void enableLogging() {
    enableLogging(HttpLoggingInterceptor.Level.BASIC);
  }

  public void enableLogging(HttpLoggingInterceptor.Level level) {
    this.okHttp = okHttp.newBuilder()
      .addInterceptor(new HttpLoggingInterceptor(log::info).setLevel(level))
      .build();
  }

  public OkHttpClient okHttp() {
    return okHttp;
  }

  public HttpClient get(String url) {
    return this.request("GET", url);
  }

  public HttpClient post(String url) {
    return this.request("POST", url);
  }

  public HttpClient delete(String url) {
    return this.request("DELETE", url);
  }

  public HttpClient put(String url) {
    return this.request("PUT", url);
  }

  public HttpClient patch(String url) {
    return this.request("PATCH", url);
  }

  public HttpClient options(String url) {
    return this.request("OPTIONS", url);
  }
  public HttpClient head(String url) {
    return this.request("HEAD", url);
  }

  public HttpClient request(String method, String url) {
    this.reqUrl = url;
    this.reqMethod = method;
    this.reqBody = null;
    this.reqHeaders.clear();
    // TODO: clear the other states

    return this;
  }

  public HttpClient json(String jsonString) {
    return body(RequestBody.create(MediaType.parse("application/json"), jsonString));
  }

  public HttpClient body(RequestBody body) {
    this.reqBody = body;

    return this;
  }

  public HttpClient emptyBody() {
    this.reqBody = RequestBody.create(MediaType.parse("text/plain"), "");

    return this;
  }

  public HttpClient header(String header, String value) {
    reqHeaders.put(header, value);

    return this;
  }

  public Response send() {
    if (reqUrl.startsWith("/")) {
      reqUrl = reqUrl.substring(1, reqUrl.length());
    }

    try {
      final HttpUrl.Builder url = requestTemplate.url()
        .newBuilder();
      final String[] segments = reqUrl.split("/");
      Arrays.stream(segments).forEach(url::addPathSegment);

      lastRequest = requestTemplate.newBuilder()
        .method(reqMethod, reqBody)
        .url(url.build())
        .headers(Headers.of(reqHeaders))
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

  public Response response() {
    return lastResponse;
  }

  public Response res() {
    return lastResponse;
  }

  public void release() {
    okHttp.dispatcher().executorService().shutdown();
    okHttp.connectionPool().evictAll();
  }

  public ResponseBody responseBody() {
    lastResponseBody = lastResponse.body();

    return lastResponseBody;
  }

  public JsonObject responseBodyJson() {
    try (final JsonReader reader = Json.createReader(new StringReader(stringResponse()))) {
      return reader.readObject();
    }
  }

  public String responseBodyString() {
    final ResponseBody body = responseBody();
    if (body != null) {
      try {
        return body.string();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      throw new RuntimeException();
    }
  }

  /** @deprecated */
  public String stringResponse() {
    log.warn("[DEPRECATED] stringResponse() called. Use responseBodyString() instead.");

    return responseBodyString();
  }

  public <T> T jsonResponse(Class<T> clz) {
    return fromJson(clz, stringResponse());
  }

  private final Moshi moshi = new Moshi.Builder()
    .add(UuidAdapter.TYPE, new UuidAdapter())
    .add(LocalDateTimeAdapter.TYPE, new LocalDateTimeAdapter())
    .add(ZonedDateTimeAdapter.TYPE, new ZonedDateTimeAdapter())
    .build();

  public <T> String toJson(Class<T> clz, T value) {
    return moshi.adapter(clz).toJson(value);
  }

  public <T> T fromJson(Class<T> clz, String value) {
    try {
      return moshi.adapter(clz).fromJson(value);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
