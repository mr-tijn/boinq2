#!/bin/bash

heap=$1

pkill -f 'java -server'

z=`echo $heap|sed "s/g//g"`;
writebuff=`echo $((z*1024*3/4))m`

# hang on until mysql is up
echo 'waiting for database'
until nc -w30 mysql 3306 < /dev/null; do
    printf '.'
    sleep 1
done

# hang on until bigdata is up
echo 'waiting for triplestore'
until $(curl --output /dev/null --silent --head --fail http://bigdata:9999); do
    printf '.'
    sleep 1
done

java -server -Xmx$heap -XX:MaxDirectMemorySize=$writebuff -Djava.security.egd=file:/dev/./urandom -jar /boinq.war --spring.profiles.active=docker
