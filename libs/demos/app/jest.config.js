module.exports = {
  name: 'demos-app',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/demos/app',
  snapshotSerializers: [
    'jest-preset-angular/AngularSnapshotSerializer.js',
    'jest-preset-angular/HTMLCommentSerializer.js'
  ]
};
