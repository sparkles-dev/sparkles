#!/bin/bash
set -e
DOMAIN=$1
if [ -z $DOMAIN ]
  then echo "usage: $0 [domain]"; exit 1
fi

# Set our CSR variables
SUBJ="
C=DE
ST=Germany
O=
localityName=BKS
commonName=*.$DOMAIN
organizationalUnitName=
emailAddress=
"

sudo openssl genrsa -out $DOMAIN.key 2048

sudo openssl req -new \
  -subj "$(echo -n "$SUBJ" | tr "\n" "/")" \
  -key $DOMAIN.key \
  -out $DOMAIN.csr

sudo openssl x509 -req -days 365 \
  -in $DOMAIN.csr \
  -signkey $DOMAIN.key  \
  -out $DOMAIN.crt
