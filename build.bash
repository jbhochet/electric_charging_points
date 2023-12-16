#!/usr/bin/env bash

# go in the project directory
cd $(dirname -- "$( readlink -f -- "$0"; )";)

# delete bin directory
rm -rf bin

# build with javac
javac -d bin --source-path src src/App.java

# build jar file
jar --create --file projet_paa.jar --main-class App -C bin/ .
