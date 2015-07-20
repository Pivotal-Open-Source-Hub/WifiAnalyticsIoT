#!/usr/bin/python
# @author wmarkito  <wmarkito@pivotal.io>
##
import os
import subprocess
import requests
import sys
import json

lattice_dir = sys.argv[1]

os.chdir(lattice_dir)
print  "Lattice directory: %s" % (os.getcwd())
container_output = subprocess.check_output("vagrant ssh -c 'curl http://127.0.0.1:7777/containers'", shell=True)
doc = json.loads(container_output)

for idx,entry in enumerate(doc["Handles"]):
    print "[%i] %s" % (idx,entry)

opt = input("-> Select container #: ")

try:
    container_info = subprocess.check_output("vagrant ssh -c 'curl http://127.0.0.1:7777/containers/%s/info'" % (doc["Handles"][opt]), shell=True)
    info_doc = json.loads(container_info)
    locator_path = info_doc["ContainerPath"]
    os.system("vagrant ssh -c 'cd %s && sudo bin/wsh' " % (locator_path))
except (IndexError):
    print "Option invalid."
