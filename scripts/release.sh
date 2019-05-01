#!/bin/bash
set -e

node_modules/.bin/standard-version \
  --message "release: cut release for version %s" \
  --prerelease mosella
