#!/usr/local/bin/R --vanilla --slave -f

library(debug)
library(rJava)
.jinit(classpath=c('.',
         'felix.jar',
         'REngine.jar'))
.jengine(TRUE)

## This is NULL, even after starting the bundle in Felix.
print(J("Activator")$getInstance())
