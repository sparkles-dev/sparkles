module.exports = {
  name: 'ar-scanner',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/apps/ar/scanner/',
  snapshotSerializers: [
    'jest-preset-angular/AngularSnapshotSerializer.js',
    'jest-preset-angular/HTMLCommentSerializer.js'
  ]
};
