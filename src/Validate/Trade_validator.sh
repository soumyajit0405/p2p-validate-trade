#!/bin/bash
cd /opt/P2P_CRON/Validate
java -cp ".:mysql-connector.jar:org.json.jar" StartThread
