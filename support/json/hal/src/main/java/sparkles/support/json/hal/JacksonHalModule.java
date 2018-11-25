package sparkles.support.json.hal;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

import sparkles.support.json.hal.deser.HalBeanDeserializerModifier;
import sparkles.support.json.hal.ser.HalBeanSerializerModifier;

public class JacksonHalModule extends Module {

  @Override
  public String getModuleName() {
    return "JacksonHalModule";
  }

  @Override
  public Version version() {
    return Version.unknownVersion();
  }

  @Override
  public void setupModule(SetupContext context) {
    context.addBeanSerializerModifier(new HalBeanSerializerModifier());
    context.addBeanDeserializerModifier(new HalBeanDeserializerModifier());
  }
}
