module.exports = {
  name: 'webcomponents-ng-app',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/apps/webcomponents/ng-app/',
  snapshotSerializers: [
    'jest-preset-angular/AngularSnapshotSerializer.js',
    'jest-preset-angular/HTMLCommentSerializer.js'
  ]
};
