#!/bin/bash
set -e
source scripts/ci/.affectedrc

yarn affected:lint ${AFFECTED_ARGS}
