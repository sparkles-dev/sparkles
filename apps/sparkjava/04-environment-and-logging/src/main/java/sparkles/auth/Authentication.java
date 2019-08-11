package sparkles.auth;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.Charset;
import sparkles.support.jwt.SimplePublicKeyProvider;

import static sparkles.support.jwt.JwtSupport.filterAuthenticatedRequests;;

public final class Authentication {

  public static void initAuth() {
    try {
      String publicKey = Resources.toString(Resources.getResource("jwt/public.key"), Charset.forName("UTF-8"));
      filterAuthenticatedRequests(new SimplePublicKeyProvider(publicKey));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
