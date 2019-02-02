package sparkles.support.json.resources;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import sparkles.support.json.resources.internal.DeserializerModifier;
import sparkles.support.json.resources.internal.LinkMixin;
import sparkles.support.json.resources.internal.SerializerModifier;

public class JsonResourcesModule extends SimpleModule {

  public JsonResourcesModule() {
    super(JsonResourcesModule.class.getSimpleName(), Version.unknownVersion());
  }

  @Override
  public void setupModule(SetupContext context) {
    context.addBeanSerializerModifier(new SerializerModifier());
    context.addBeanDeserializerModifier(new DeserializerModifier());
    context.setMixInAnnotations(Link.class, LinkMixin.class);
  }

  // TODO:
  // - inspire from https://github.com/openapi-tools/jackson-dataformat-hal
  // (/) add serializer + modifier
  // (/) add deserializer + modifier
  // - change serialization and deserialization...
  //    (/) recognize custom @Embedded annotation
  //    - for @Links annotation and Link class

}
