README for boinq
==========================
For testing purposes, a docker setup is provided. It contains the boinq system, a database and a triplestore (blazegraph).
Build instructions (from source):
* mvn -Dmaven.test.skip=true -Pprod package
* mv target/boinq-0.9-SNAPSHOT.war docker/boinq/build
* docker-compose build
The system is accessible on port 80 on the default docker machine. Obtain this ip address by:
* docker-machine ip default

For other use, modify the properties in the application-prod.yml, rebuild with maven and either
* install the war on an application server
* launch the built-in jetty server: java -jar boinq.war --spring.profiles.active=prod
