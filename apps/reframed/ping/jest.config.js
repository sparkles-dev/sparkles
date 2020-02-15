module.exports = {
  name: 'reframed-ping',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/apps/reframed/ping/',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js'
  ]
};
