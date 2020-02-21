module.exports = {
  name: 'rivers-rivers',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/apps/rivers/rivers/',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js'
  ]
};
