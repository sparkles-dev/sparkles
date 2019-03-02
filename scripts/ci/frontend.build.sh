#!/bin/bash
set -e
source scripts/ci/.affectedrc

yarn packages:styles:build
yarn affected:build ${AFFECTED_ARGS}
