import sys
import os
import subprocess
import time
import re
import hashlib
import socket
import urllib
import urllib2

HOSTNAME = socket.gethostname()
SERVER_ENDPOINT = 'http://127.0.0.1:8000/agent/report/'


def main_loop():
    # Remove the log file if present
    try:
        os.unlink('tshark.log')
        print("deleted log")
    except:
        print("no log to delete")
        pass
    # start the tshark process, outputting to file
    print("opening process")
    # proc = subprocess.Popen("stdbuf -oL tshark -i wlan0 -I -f 'broadcast' -R 'wlan.fc.type == 0 && wlan.fc.subtype == 4' -T fields -e frame.time_epoch -e wlan.sa -e radiotap.dbm_antsignal > tshark.log",
    proc = subprocess.Popen("stdbuf -oL tshark -i en0 -I -f 'broadcast' -Y 'wlan.fc.type == 0 && wlan.fc.subtype == 4' -T fields -e frame.time_epoch -e wlan.sa -e radiotap.dbm_antsignal > tshark.log",
        shell=True,
        bufsize=1,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT)
    # give the process a few seconds to open its log file
    print("sleeping...")
    time.sleep(3)
    # open the log file
    log = open('tshark.log', 'r+')
    print("log opened")
    sightings = []
    try:
        # start process loop
        while (True):
            # report any sightings
            report(sightings)
            # read in one line of log
            line = log.readline()
            # empty string is sent back if no input available
            if line == "":
                # is the process dead? if so stop reading log
                if proc.poll() is not None:
                    print("process died")
                    break
                # wait a second so we aren't hammering the log file
                time.sleep(1)
            else:
                # see if the input line matches our expected format
                match = re.search(r"(\d+)\.\d+\t([a-z0-9:]+)\t([0-9-]+)\n", line)
                if match is not None:
                    sightings.append(match.groups())
    except KeyboardInterrupt:
        clean_up(proc, log)
        print("exited by command")
        sys.exit()
    # clean up
    clean_up(proc, log)


def report(sightings):
    # if there are any sightings queued for reporting, send to server
    for index in range(len(sightings)):
        sighting = sightings[index]
        data = {
            'host': HOSTNAME,
            'timestamp': sighting[0],
            'device_id': hashlib.md5(sighting[1]).hexdigest(),
            'signal_dbm': sighting[2]
        }
        del sightings[index]
        print(data)
        # don't crash if the request fails
        try:
            req = urllib2.urlopen(SERVER_ENDPOINT, urllib.urlencode(data))
            req.close()
        except:
            pass


def clean_up(proc, log):
    proc.terminate()
    proc.poll()
    log.close()


if __name__ == '__main__':
    # start main loop
    while (True):
        main_loop()
