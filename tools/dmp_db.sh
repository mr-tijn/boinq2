#!/bin/bash

./liquibase.sh --classpath=/Users/martijn/.m2/repository/mysql/mysql-connector-java/5.1.35/mysql-connector-java-5.1.35.jar --driver=com.mysql.jdbc.Driver --changeLogFile=./createScript.xml --url=jdbc:mysql://localhost:3306/boinq --username=boinq --password=B01Nqkq\!! generateChangeLog
