#!/usr/bin/env bash
sudo kill $(ps -ef | grep 'sudo nohup java -jar cast-a-spell-server-0.1.0.jar' | head -n 1 | grep -o -E '[0-9]+' | head -1 | sed -e 's/^0\+//')