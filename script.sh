#!/bin/bash
set -ev

if [ "$FOLDER" = "lab6" ]; then
    if [ "$OSTYPE" = "darwin17" ]; then # OS = osx
        wget https://github.com/forax/java-next/releases/download/untagged-c59655314c1759142c15/jdk-14-loom-osx.tar.gz
        tar xvf jdk-14-loom-osx.tar.gz
    else
        wget https://github.com/forax/java-next/releases/download/untagged-c59655314c1759142c15/jdk-14-loom-linux.tar.gz
        tar xvf jdk-14-loom-linux.tar.gz
    fi
    export JAVA_HOME=$(pwd)/jdk-14-loom/
fi

cd $FOLDER
mvn package
