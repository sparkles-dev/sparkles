#!/bin/sh
set -e

### Validate arguments
PACKAGE_NAME=${1}
if [ -z ${PACKAGE_NAME} ]; then
    echo "ERROR: Which package should be deployed?"
    echo "Usage: ./tools/verdaccio/publish.sh <packageName>"
    echo "Package Name should be a folder name in packages"
    exit 1
fi

REGISTRY=//localhost:4873
REGISTRY_URL=http:${REGISTRY}

### Login with credentials to local registry
if ! grep -qF "$REGISTRY" ~/.npmrc ;then
    echo "Need authentication for local registry..."
    NPM_USER=sparkles \
    NPM_PASS=lights \
    NPM_EMAIL=lights@noreply.sparkles.example.com \
    NPM_REGISTRY=$REGISTRY_URL \
    node_modules/.bin/npm-cli-login
    echo "Added npm user authentication to ~/.npmrc"
fi

### Publish package
node scripts/version.js ${PACKAGE_NAME}

npm publish \
    packages/${PACKAGE_NAME}/dist \
    --registry ${REGISTRY_URL} \
    --tag next
