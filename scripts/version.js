#!/usr/bin/env node
"use strict";
const fs = require('fs');
const path = require('path');
const generateVersionInfo = require('../tools/version/git-describe');

// Read CLI arguments
const ARGS = require('minimist')(process.argv.slice(2));
if (typeof ARGS._[0] !== 'string') {
  console.log(`Usage: ./scripts/version.js <PACKAGE_NAME>`);
  process.exit(1);
}

// Resolve directory paths
const ROOT_DIR    = path.resolve(__dirname, '..');
const PKG_DIR     = path.resolve(ROOT_DIR, 'packages');
const FILE_NAME   = path.resolve(PKG_DIR, ARGS._[0], 'dist', 'package.json');

function writePackageJson(fileName, additionalProperties) {

  return new Promise((resolve, reject) => {
    const packageJson = JSON.parse(fs.readFileSync(fileName));

    // overwrite properties
    if (additionalProperties) {
      Object.keys(additionalProperties).forEach(key => {
        packageJson[key] = additionalProperties[key];
      });
    }

    fs.writeFileSync(fileName, JSON.stringify(packageJson, undefined, 2));

    resolve();
  });
}

function updatePackageJson(fileName) {
  const version = generateVersionInfo();

  if (version) {
    const publishConfig = {
      registry: 'http://localhost:4873/'
    };

    writePackageJson(fileName, { version, publishConfig });
  } else {
    writePackageJson(fileName);
  }
}

updatePackageJson(FILE_NAME);
