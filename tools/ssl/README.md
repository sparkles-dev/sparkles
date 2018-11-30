SSL Certificates
################

 - Truststore: A trust store is used to authenticate peers. The `cacerts` file is where Java stores public certificates of trusted root CAs.
 - Keystore: A keystore is used to authenticate yourself.

List certificates in the java truststore:

```bash
$ keytool -list \
  -storepass changeit \
  -keystore /opt/java/jre/lib/security/cacerts
```

Import a certificate to the java truststore:

```bash
keytool -import \
  -file my.cert.location/my.cert.crt \
  -storepass changeit \
  -keystore $JAVA_HOME/jre/lib/security/cacerts -alias mycert1
```

Location of default truststore on Mac OS:

```
/System/Library/Frameworks/JavaVM.framework/Home/lib/security/cacerts
```

Running JavaVM with a custom truststore/keystore:

```bash
$ java  \
  -Djavax.net.ssl.truststore=path/to/truststore \
  -Djavax.net.ssl.trustStorePassword=changeit \
  -Djavax.net.ssl.keyStore=path/to/keystore \
  -Djavax.net.ssl.keyStorePassword=changeit \
  <other-args>
```

Running JavaVM with custom truststore/keystore (via environment variables):

```bash
JAVA_OPTS=$JAVA_OPTS \
  -Djavax.net.ssl.truststore=path/to/truststore \
  -Djavax.net.ssl.trustStorePassword=changeit \
  -Djavax.net.ssl.keyStore=path/to/keystore \
  -javax.net.ssl.keyStorePassword=changeit
```

