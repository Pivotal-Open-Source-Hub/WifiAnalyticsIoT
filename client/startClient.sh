mkfifo probe_pipe
nohup java -jar IotAnalytics-all.jar >> client.out &
