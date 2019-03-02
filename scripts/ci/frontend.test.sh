#!/bin/bash
set -e
source scripts/ci/.affectedrc

xvfb-run -a yarn affected:test ${AFFECTED_ARGS}
