'use strict';

import gulp from 'gulp';
import postcss from 'gulp-postcss';
import tailwindcss from 'tailwindcss';
import autoprefixer from 'autoprefixer';
import sass from 'gulp-sass';
import nodeSass from 'node-sass';

sass.compiler = nodeSass;

function css() {
  return gulp.src('src/styles.scss')
    .pipe(
      sass().on('error', sass.logError),
    )
    .pipe(postcss([
      tailwindcss('tailwind.js'),
      autoprefixer
    ]))
    .pipe(gulp.dest('build/'));
}

exports.default = css;
