#!/bin/bash

MY_PATH="`dirname \"$0\"`"              # relative
LOG_PATH="`( cd \"$MY_PATH\" && cd .. && pwd )`/log/network.log"
now=$(date +"%m-%d %r")

# Which Interface do you want to check
wlan='wlan0'
# Which address do you want to ping to see if you can connect
pingip='google.com'

# Perform the network check and reset if necessary
/bin/ping -c 2 -I $wlan $pingip > /dev/null 2> /dev/null
if [ $? -ge 1 ] ; then
    echo "$now Network is DOWN. Perform a reset" >> $LOG_PATH
    sudo sbin/ifdown $wlan
    sleep 5
    sudo /sbin/ifup --force $wlan
else
    echo "$now Network is UP. Just exit the program." >> $LOG_PATH
fi
