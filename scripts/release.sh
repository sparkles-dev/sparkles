#!/bin/bash
set -e

node_modules/.bin/standard-version \
  --releaseCommitMessageFormat "release: cut release for version {{currentTag}}" \
  --prerelease mosella
