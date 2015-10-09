#!/bin/bash
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
sed -i '' "s/\(.*htsjdk.version>\)\([^<]*\)\(<\/htsjdk.version*\)/\1$VER\3/g" ../pom.xml
