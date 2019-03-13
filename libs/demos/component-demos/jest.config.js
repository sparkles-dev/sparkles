module.exports = {
  name: 'demos-component-demos',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/demos/component-demos',
  snapshotSerializers: [
    'jest-preset-angular/AngularSnapshotSerializer.js',
    'jest-preset-angular/HTMLCommentSerializer.js'
  ]
};
