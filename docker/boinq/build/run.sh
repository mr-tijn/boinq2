#!/bin/bash

heap=$1

pkill -f 'java -server'

z=`echo $heap|sed "s/g//g"`;
writebuff=`echo $((z*1024*3/4))m`

# hang on until mysql is up
print 'waiting for database'
until nc -w30 mysql 3306 < /dev/null; do
    printf '.'
    sleep 1
done

# hang on until bigdata is up
print 'waiting for triplestore'
until $(curl --output /dev/null --silent --head --fail http://bigdata:9999); do
    printf '.'
    sleep 1
done

# create namespaces

curl -v --connect-timeout 30 -X POST --data-binary @/createBoinq.xml --header 'Content-Type:application/xml' http://bigdata:9999/bigdata/namespace
curl -v -X POST --data-binary @/createBoinqStatic.xml --header 'Content-Type:application/xml' http://bigdata:9999/bigdata/namespace
curl -v -X POST --data-binary @/createTest.xml --header 'Content-Type:application/xml' http://bigdata:9999/bigdata/namespace

java -server -Xmx$heap -XX:MaxDirectMemorySize=$writebuff -Djava.security.egd=file:/dev/./urandom -jar /boinq.war --spring.profiles.active=docker
