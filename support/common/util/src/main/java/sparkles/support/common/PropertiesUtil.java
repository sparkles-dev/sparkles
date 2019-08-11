package sparkles.support.common;

import com.google.common.io.Resources;

import java.io.InputStream;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Properties;

import sparkles.support.common.functional.OptionalUtil;
import sparkles.support.common.functional.ThrowingSupplier;

public final class PropertiesUtil {

  private final Properties properties;

  private PropertiesUtil(Properties properties) {
    this.properties = properties;
  }

  public PropertiesUtil merge(PropertiesUtil other) {
    properties.putAll(other.properties);

    return this;
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public String getProperty(String key, String defaultValue) {
    return properties.getProperty(key, defaultValue);
  }

  private static PropertiesUtil emptyProperties() {
    return new PropertiesUtil(new Properties());
  }

  private static PropertiesUtil fromSupplierOrEmpty(ThrowingSupplier<PropertiesUtil> supplier) {
    return OptionalUtil.fromThrowing(supplier)
      .orElse(emptyProperties());
  }

  public static PropertiesUtil readApplicationProperties() {
    return fromSupplierOrEmpty(() -> readPropertiesResource("application.properties"))
      .merge(fromSupplierOrEmpty(() -> readPropertiesFile("application.properties")));
  }

  public static PropertiesUtil readPropertiesFile(String fileName) throws IOException, FileNotFoundException {
    final FileReader fileReader = new FileReader(fileName);
    final Properties properties = new Properties();
    properties.load(fileReader);

    return new PropertiesUtil(properties);
  }

  public static PropertiesUtil readPropertiesResource(String resourceName) throws IOException {
    final InputStream inputStream = Resources.getResource(resourceName).openStream();
    final Properties properties = new Properties();
    properties.load(inputStream);

    return new PropertiesUtil(properties);
  }
}
