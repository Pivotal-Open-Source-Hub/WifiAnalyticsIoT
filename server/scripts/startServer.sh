#!/bin/sh
mkdir -p /data/server/$HOSTNAME

sleep 10

gfsh start server --name=$HOSTNAME --locators=locator[10334] --dir=/data/server/$HOSTNAME/ "$@"

while true; do
    sleep 10
  done
done
