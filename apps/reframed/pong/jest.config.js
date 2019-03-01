module.exports = {
  name: 'reframed-pong',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/apps/reframed/pong/',
  snapshotSerializers: [
    'jest-preset-angular/AngularSnapshotSerializer.js',
    'jest-preset-angular/HTMLCommentSerializer.js'
  ]
};
