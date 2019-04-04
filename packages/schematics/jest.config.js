module.exports = {
  name: 'packages-schematics',
  preset: '../../jest.config.js',
  coverageDirectory: '../../coverage/packages/schematics',
  snapshotSerializers: [
    'jest-preset-angular/AngularSnapshotSerializer.js',
    'jest-preset-angular/HTMLCommentSerializer.js'
  ]
};
