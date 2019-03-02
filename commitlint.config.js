module.exports = {
  extends: ['@commitlint/config-angular'],
  rules: {
    'header-max-length': [2, 'always', 120],
    'scope-enum': [
      2,
      'always',
      [
        'domain',
        'components',
        'forms',
        'reframed',
        'shared',
        'styles',
        'testing'
      ]
    ],
    'scope-empty': [1, 'never'],
    'type-enum': [
      2,
      'always',
      [
        'build',
        'ci',
        'docs',
        'feat',
        'fix',
        'perf',
        'refactor',
        'release',
        'revert',
        'style',
        'test'
      ]
    ]
  }
};
