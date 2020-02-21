module.exports = {
  name: 'demos-app',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/demos/app',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js'
  ]
};
