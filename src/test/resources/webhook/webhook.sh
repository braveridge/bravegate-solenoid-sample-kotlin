#!/bin/bash

SCRIPT_DIR=$(cd $(dirname $0); pwd)

curl -H 'Content-Type:application/json' -X POST http://localhost:8080/solenoid/webhook -d @$SCRIPT_DIR/json/solenoid.json
