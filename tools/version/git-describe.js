"use strict";
const git = require('git-describe');

/**
 * Generate a version info string or return an `undefined` value.
 *
 * @returns When `undefined` is returned, the version info from the `package.json` property `version` should be used.
 */
function generateVersionInfo() {
  const versionInfo = git.gitDescribeSync({ longSemver: true });

  if (versionInfo.dirty || versionInfo.distance) {
    let versionString = versionInfo.raw.startsWith('v') ? versionInfo.raw.substr(1) : versionInfo.raw;
    if (versionInfo.dirty) {
      versionString = `${versionString}-${Date.now().toString()}`;
    }
    if (!versionInfo.tag) {
      versionString = `0.0.0-${versionString}`;
    }

    return versionString;
  }

  return undefined;
}

module.exports = generateVersionInfo;
