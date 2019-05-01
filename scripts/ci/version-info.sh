#!/bin/bash

echo "Node: $(node --version)"
echo "Yarn: $(yarn --version)"
echo "NX: $(node_modules/.bin/nx --version)"
# Angular CLI prints a pretty multi-line version info
node_modules/.bin/ng --version
