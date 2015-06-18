#!/bin/bash

export GEODE_HOME=/Users/wmarkito/Pivotal/ASF/incubator-geode/gemfire-assembly/build/install/apache-geode/
export PATH=$GEODE_HOME/bin:$PATH

echo "Geode version:" `gfsh version`
gfsh run --file=setup.gfsh



