#!/bin/bash

cp ../../../target/boinq-1.0.0-ALPHA.war ./boinq.war
docker build --iidfile=latest.id --tag=md3v/boinq:latest .
