package sparkles.support.keycloak;

import sparkles.support.keycloak.jwk.JSONWebKeySet;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class KeycloakTests {

  // <baseUrl>/realms/{realm}/protocol/openid-connect/certs
  @Test
  @Ignore
  public void foo() throws IOException {
    final JwksApi api = new JwksApi.Builder()
      .baseUrl("http://localhost:9999/auth")
      .build();

    final JSONWebKeySet keySet = api.getJsonWebKeySet("orbis").execute().body();

    assertThat(keySet.keys).hasSize(1);
  }

}
