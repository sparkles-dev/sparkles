package sparkles;

import io.javalin.Javalin;

import sparkles.entity.FooApi;

import sparkles.support.common.Environment;
import sparkles.support.javalin.BaseApp;
import sparkles.support.javalin.keycloak.security.KeycloakAccessManager;
import sparkles.support.javalin.keycloak.security.KeycloakRoles;
import sparkles.support.javalin.spring.data.auditing.AuditingExtension;

public class EntityApp {

  public static void main(String[] args) {
    new EntityApp().init().start();
  }

  public Javalin init() {

    return BaseApp.build("entity")
      .register(AuditingExtension.create((ctx) -> {
        // TODO: resolve auditor from request context
        return "foo";
      }))
      .accessManager(KeycloakAccessManager.create(
        Environment.value("KEYCLOAK_URL",  "https://foobar"),
        Environment.value("KEYCLOAK_REALM", "realm"),
        (claims) -> {
          // TODO: resolve role from keycloak stuff
          return KeycloakRoles.ANYONE;
        }
      ))
      .register(new FooApi());

  }

}
