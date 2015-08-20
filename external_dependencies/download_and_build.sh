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
curl -Lo htsjdk.zip https://github.com/samtools/htsjdk/zipball/master
unzip htsjdk.zip
DIR=`find . -maxdepth 1 -type d -name '*htsjdk*'`
cd $DIR
ant htsjdk-jar
LIB=`cd dist;ls htsjdk-*.jar`
VER=${LIB%.jar}
VER=${VER##*-}
ART=${LIB%-$VER.jar}
cp dist/$LIB ../lib
cd ..
mvn install:install-file \
 -Dfile=lib/$LIB \
 -DgroupId=broad \
 -DartifactId=$ART \
 -Dversion=$VER \
 -Dpackaging=jar \
 -DgeneratePom=true
rm -rf $DIR
rm htsjdk.zip
sed -i '' "s/\(.*htsjdk.version>\)\([^<]*\)\(<\/htsjdk.version*\)/\1$VER\3/g" ../pom.xml
