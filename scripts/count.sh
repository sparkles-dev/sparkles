#!/bin/bash

EXT=${1-ts}
find . \
  -name "*.${EXT}" \
  -not -path "./node_modules/*" \
  -not -path "*/build/*" \
  -not -path "./coverage/*" \
  | xargs wc -l
