#!/bin/bash

if [ -z "$GEODE_HOME" ]; then
    echo "Please set GEODE_HOME"
    exit 1
fi

if [ -z "$JAVA_HOME" ]; then
    echo "Please set JAVA_HOME"
    exit 1
fi

export PATH=$GEODE_HOME/bin:$PATH

echo "Geode version:" `gfsh version`
gfsh run --file=setup.gfsh



