package sparkles.support.javalin.spring.data.rest;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.javalin.Context;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import sparkles.support.common.collections.CollectionUtil;

import static sparkles.support.javalin.spring.data.SpringDataExtension.springData;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RestRepositoryHandler<Repo extends CrudRepository<Entity, ID>, Entity, ID> {
  private final Class<Repo> repoClz;
  private final Class<Entity> entityClz;

  private Repo repository(Context ctx) {
    return springData(ctx).repository(repoClz);
  }

  protected abstract String toId(Entity entity);

  protected EntityResource<Entity> toResource(Context ctx, Entity entity) {
    final EntityResource<Entity> resource = new EntityResource<>();
    resource.entity = entity;
    resource.withSelfRel(entityUrl(ctx, entity));

    return resource;
  }

  protected EntityCollectionResource<Entity> toResourceCollection(Context ctx, List<Entity> entities) {
    final EntityCollectionResource resource = EntityCollectionResource.from(
      entities.stream().map(e -> toResource(ctx, e)).collect(Collectors.toList())
    );
    resource.withSelfRel(ctx.path());

    return resource;
  }

  protected String entityUrl(Context ctx, Entity entity) {
    return ctx.path() + "/" + toId(entity);
  }

  public void findAll(Context ctx) {
    Iterable<Entity> iterable = repository(ctx).findAll();
    List<Entity> entities = CollectionUtil.toList(iterable);

    ctx.status(200);
    ctx.json(toResourceCollection(ctx, entities));
  }

  public void create(Context ctx) {
    Entity entity = ctx.bodyAsClass(entityClz);
    Entity result = repository(ctx).save(entity);

    ctx.status(201);
    ctx.header("Location", entityUrl(ctx, result));
    ctx.json(toResource(ctx, result));
  }

  public void update(Context ctx) {
    Entity entity = ctx.bodyAsClass(entityClz);
    Entity result = repository(ctx).save(entity);

    final EntityResource resource = new EntityResource();
    resource.entity = result;

    ctx.status(200);
    ctx.json(resource);
  }

  public void exists(Context ctx, ID id) {
    if (repository(ctx).existsById(id)) {
      ctx.status(200);
    } else {
      ctx.status(404);
    }
  }

  public void findOne(Context ctx, ID id) {
    Optional<Entity> entity = repository(ctx).findById(id);

    if (entity.isPresent()) {
      final EntityResource resource = new EntityResource();
      resource.entity = entity;

      ctx.status(200);
      ctx.json(resource);
    } else {
      ctx.status(404);
    }
  }

  public void delete(Context ctx, ID id) {
    repository(ctx).deleteById(id);

    ctx.status(200);
  }

}
