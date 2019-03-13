module.exports = {
  name: 'package-demos-component-demos',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/package-demos/component-demos',
  snapshotSerializers: [
    'jest-preset-angular/AngularSnapshotSerializer.js',
    'jest-preset-angular/HTMLCommentSerializer.js'
  ]
};
