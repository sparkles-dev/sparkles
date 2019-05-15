module.exports = {
  name: 'ar-code-scanner',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/ar/code-scanner',
  snapshotSerializers: [
    'jest-preset-angular/AngularSnapshotSerializer.js',
    'jest-preset-angular/HTMLCommentSerializer.js'
  ]
};
