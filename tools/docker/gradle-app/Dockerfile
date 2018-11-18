FROM java:openjdk-8-jdk

###
# Builds args cannot be consumed by an entrypoint, provide here to write a start.sh script
ARG bin
ARG zip

ENV ENVIRONMENT PRODUCTION

###
# SSL certificates: mark self-signed root CA trusted
#RUN update-ca-trust force-enable \
#    && cp /run/secrets/ssl.crt /etc/pki/ca-trust/source/anchors/ \
#    && update-ca-trust extract

###
# Copy build artefacts to container
RUN mkdir -p /app
COPY *.zip /app
RUN cd /app && unzip -o *.zip
RUN echo '#!/bin/sh' > /app/start.sh \
 && echo "/app/${zip}/bin/${bin}" >> /app/start.sh \
 && chmod +x /app/start.sh

ENTRYPOINT ["/app/start.sh"]
