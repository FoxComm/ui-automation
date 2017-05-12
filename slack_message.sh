#!/bin/bash

if [ "$EXIT_CODE" = 0 ]; then
	PAYLOAD='payload={ "attachments": [{"pretext": "Tests Passed", "color": "good", "text": "<http://10.240.0.32:8080/#/|View Report>"}] }'
elif [ "$EXIT_CODE" = 1 ]; then
	PAYLOAD='payload={ "attachments": [{"pretext": "Tests Failed", "color": "#B94A48", "text": "<http://10.240.0.32:8080/#/|View Report>"}] }'
fi

curl -X POST --data-urlencode "$PAYLOAD" $HOOK
