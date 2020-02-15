module.exports = {
  name: 'webcomponents-demo-element',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/apps/webcomponents/demo-element/',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js'
  ]
};
