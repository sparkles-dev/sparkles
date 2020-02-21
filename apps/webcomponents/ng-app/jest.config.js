module.exports = {
  name: 'webcomponents-ng-app',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/apps/webcomponents/ng-app/',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js'
  ]
};
