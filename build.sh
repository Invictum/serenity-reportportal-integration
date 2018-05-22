#!/bin/bash

# Build script used by Travis CI

set -ev
echo "Active branch: $1"

case "$1" in
"develop")
    echo "Snapshot publisher will be applied"
    mvn --settings custom-settings.xml clean deploy
    ;;
"master")
    echo "Master build is disabled"
    ;;
*)
    echo "PR builder will be applied"
    mvn clean package
    ;;
esac
