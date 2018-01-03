#!/bin/bash

cp ../target/boinq-1.0.0-ALPHA.war boinq/build/
docker-compose build
