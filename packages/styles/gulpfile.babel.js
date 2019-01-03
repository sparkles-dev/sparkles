'use strict';

import gulp from 'gulp';
import postcss from 'gulp-postcss';
import tailwindcss from 'tailwindcss';
import autoprefixer from 'autoprefixer';
import postcssImport from 'postcss-import';

function css() {
  return gulp.src('src/styles.css')
    .pipe(postcss([
      postcssImport,
      tailwindcss('tailwind.js'),
      autoprefixer
    ]))
    .pipe(gulp.dest('build/'));
}

exports.default = css;
