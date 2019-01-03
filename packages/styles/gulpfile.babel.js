'use strict';

import gulp from 'gulp';
import postcss from 'gulp-postcss';
import tailwindcss from 'tailwindcss';
import autoprefixer from 'autoprefixer';

function css() {
  return gulp.src('src/styles.css')
    .pipe(postcss([
      tailwindcss('tailwind.js'),
      autoprefixer
    ]))
    .pipe(gulp.dest('build/'));
}

exports.default = css;
