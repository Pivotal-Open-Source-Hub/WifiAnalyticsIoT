sudo airmon-ng start wlan1 6
#sudo tshark -i wlan1mon -I -f 'broadcast' -R 'wlan.fc.type == 0 && wlan.fc.subtype == 4' -T fields -e frame.time_epoch -e wlan.sa -e radiotap.dbm_antsignal
