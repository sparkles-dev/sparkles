package sparkles.entity;

import io.javalin.Javalin;

import java.util.List;

import io.javalin.core.plugin.Plugin;
import lombok.extern.slf4j.Slf4j;
import sparkles.support.javalin.security.jwt.JwtRoles;
import sparkles.support.javalin.springdata.auditing.Auditing;

import static io.javalin.apibuilder.ApiBuilder.crud;
import static sparkles.support.javalin.BaseApp.requires;
import static sparkles.support.javalin.springdata.SpringDataExtension.springData;

@Slf4j
public class FooApi implements Plugin {

  @Override
  public void apply (Javalin app) {

    app.get("/", (ctx) -> {
      FooRepository repository = springData(ctx).createRepository(FooRepository.class);

      List<FooEntity> stuffs = repository.findAll();
      for (FooEntity stuff : stuffs) {
        log.info("stuff is {} // {}", stuff.createdAt, stuff.createdBy);
      }

      ctx.result("count: " + stuffs.size());
    }, requires(JwtRoles.ANYONE))
    .post("/", (ctx) -> {
      Object auditor = Auditing.getStrategy().resolveCurrentContext().getCurrentAuditor().get();
      log.info("Current auditor is {}", auditor);

      FooEntity e = new FooEntity().withName("foobararar").addKind(FooEntity.Kind.FOO);
      if (Math.random() < 0.5) {
        e.addKind(FooEntity.Kind.FOOBAR);
      }

      FooRepository repository = springData(ctx).createRepository(FooRepository.class);
      FooEntity entity = repository.save(e);

      ctx.json(entity).status(201);
    });

    /* https://github.com/tipsy/javalin/issues/709#issuecomment-521722888
    app.routes(() -> {
      crud("stuff/:id", crudHandler(FooRepository.class, FooEntity.class, UUID::fromString));
    });
    */

  }
}
