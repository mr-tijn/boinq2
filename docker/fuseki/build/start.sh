#!/bin/bash

cat latest.id | xargs docker run -p 3030:3030
