module.exports = {
  name: 'rivers-rivers',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/apps/rivers/rivers/',
  snapshotSerializers: [
    'jest-preset-angular/AngularSnapshotSerializer.js',
    'jest-preset-angular/HTMLCommentSerializer.js'
  ]
};
