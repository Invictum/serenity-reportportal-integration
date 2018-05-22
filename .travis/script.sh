#!/usr/bin/env bash

ACTIVE_BRANCH=$1
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
