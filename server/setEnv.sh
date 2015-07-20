#!/bin/bash

# Replace with all proper paths after installing packages

GEODE_HOME=~/apache-geode-1.0.0-incubating-SNAPSHOT/
ZEPPELIN_HOME=~/zeppelin-0.6.0-incubating-SNAPSHOT/
SPRINGXD_HOME=~/spring-xd-1.2.0.RELEASE/
GRADLE_HOME=~/gradle-2.4/
MAVEN_HOME=~/apache-maven-3.3.3/

# set PATH
export PATH=$GEODE_HOME/bin:$ZEPPELIN_HOME/bin:$SPRINGXD_HOME/shell/bin:$SPRINGXD_HOME/xd/bin:$GRADLE_HOME/bin:$MAVEN_HOME/bin:$PATH
