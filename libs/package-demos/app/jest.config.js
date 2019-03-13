module.exports = {
  name: 'package-demos-app',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/package-demos/app',
  snapshotSerializers: [
    'jest-preset-angular/AngularSnapshotSerializer.js',
    'jest-preset-angular/HTMLCommentSerializer.js'
  ]
};
