module.exports = {
  name: 'ar-webrtc-media',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/ar/webrtc-media',
  snapshotSerializers: [
    'jest-preset-angular/AngularSnapshotSerializer.js',
    'jest-preset-angular/HTMLCommentSerializer.js'
  ]
};
