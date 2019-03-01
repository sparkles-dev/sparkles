module.exports = {
  name: 'reframed-ping',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/apps/reframed/ping/',
  snapshotSerializers: [
    'jest-preset-angular/AngularSnapshotSerializer.js',
    'jest-preset-angular/HTMLCommentSerializer.js'
  ]
};
