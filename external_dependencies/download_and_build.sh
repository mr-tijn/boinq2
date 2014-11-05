#!/bin/bash
curl -Lo java-genomics-io-master.zip https://github.com/timpalpant/java-genomics-io/archive/master.zip
unzip java-genomics-io-master.zip
cd java-genomics-io-master
ant dist
cp dist/java-genomics-io.jar ../lib
cd ..
mvn install:install-file \
-Dfile=lib/java-genomics-io.jar \
-DgroupId=edu.unc \
-DartifactId=java-genomics-io \
-Dversion=master \
-Dpackaging=jar \
-DgeneratePom=true
rm -rf java-genomics-io-master
rm java-genomics-io-master.zip
