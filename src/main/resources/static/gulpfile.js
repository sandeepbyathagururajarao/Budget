var gulp        = require('gulp');
var browserSync = require('browser-sync');

gulp.task('browser-sync', function() {
    browserSync({
        files: "webapp/*.html",
        server: {
            baseDir: "." // Change this to your web root dir
        }
    });
});

// Default task to be run with `gulp`
gulp.task('default', ["browser-sync"]);