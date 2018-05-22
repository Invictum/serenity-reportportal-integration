#!/bin/bash

set -ev
ACTIVE_BRANCH=${TRAVIS_PULL_REQUEST}

echo "Branch: $ACTIVE_BRANCH"

case "$ACTIVE_BRANCH" in
"develop")
    echo "Develop"
    ;;
"master")
    echo "Master"
    ;;
*)
    echo "Default"
    ;;
esac
