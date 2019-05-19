module.exports = {
  name: 'webcomponents-demo-element',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/apps/webcomponents/demo-element/',
  snapshotSerializers: [
    'jest-preset-angular/AngularSnapshotSerializer.js',
    'jest-preset-angular/HTMLCommentSerializer.js'
  ]
};
