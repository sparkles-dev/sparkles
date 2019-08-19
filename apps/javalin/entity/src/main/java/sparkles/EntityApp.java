package sparkles;

import io.javalin.Javalin;

import sparkles.entity.FooApi;

import sparkles.support.common.Environment;
import sparkles.support.javalin.BaseApp;
import sparkles.support.javalin.flyway.FlywayPlugin;
import sparkles.support.javalin.security.jwt.JwtRoles;
import sparkles.support.javalin.security.keycloak.KeycloakAccessManager;
import sparkles.support.javalin.springdata.auditing.AuditingPlugin;

public class EntityApp {

  public static void main(String[] args) {
    new EntityApp().init().start();
  }

  public Javalin init() {

    AuditingPlugin auditing = AuditingPlugin.create((ctx) -> {
      // TODO: resolve auditor from request context
      return "foo";
    });

    return javalin = BaseApp.customize("entity")
      .with(cfg -> {
        cfg.accessManager(KeycloakAccessManager.create(
          Environment.value("KEYCLOAK_URL",  "https://foobar"),
          Environment.value("KEYCLOAK_REALM", "realm"),
          (claims) -> {
            // TODO: resolve role from keycloak stuff
            return JwtRoles.ANYONE;
          }
        ));

        cfg.getPlugin(FlywayPlugin.class).runBeforeServerStart(true);
      })
      .create(auditing, new FooApi());
  }

}
