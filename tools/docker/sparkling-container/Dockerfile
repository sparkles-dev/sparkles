FROM java:openjdk-8-jdk

ARG bin
ARG zip

###
# Copy build artefacts to container
RUN mkdir -p /app
COPY dist/*.zip /app
RUN cd /app && unzip *.zip
RUN echo '#!/bin/sh' > /app/start.sh &&  echo "/app/${zip}/bin/${bin}" >> /app/start.sh && chmod +x /app/start.sh

ENTRYPOINT ["/app/start.sh"]
