module.exports = {
  name: 'reframed-pong',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/apps/reframed/pong/',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js'
  ]
};
