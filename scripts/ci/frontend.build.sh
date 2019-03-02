#!/bin/bash
set -e
source scripts/ci/.affectedrc

yarn affected:build ${AFFECTED_ARGS}
