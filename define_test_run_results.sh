#!/bin/bash
# Determines if test run was successful or not based on tests result and their severity.

BROKENS=0
MINORS=0
NORMALS=0
CRITICALS=0
BLOCKERS=0

# get "testCases" json array from graph.json, then get its length
TOTAL_TESTS=$(cat allure-report/data/graph.json | jq '.testCases' | jq '. | length')

# iterate through testCases json array
# get $severity and $status for each test
# if status equals "FAILED" then increase amount of failed tests with certain $severity
count_failures() {
    for (( i=0; i<$TOTAL_TESTS; i++ )); do

        severity=($(cat allure-report/data/graph.json | jq ".testCases[$i].severity"))
        status=($(cat allure-report/data/graph.json | jq ".testCases[$i].status"))

        if [[ "$status" == "\"FAILED\"" ]]; then

            case "$severity" in
                "\"MINOR\"") MINORS=$((MINORS+1)) ;;
                "\"NORMAL\"") NORMALS=$((NORMALS+1)) ;;
                "\"CRITICAL\"") CRITICALS=$((CRITICALS+1)) ;;
                "\"BLOCKER\"") BLOCKERS=$((BLOCKERS+1)) ;;
            esac
        elif [[ "$status" == "\"BROKEN\"" ]]; then
                BROKENS=$((BROKENS+1))
        fi

    done
}

# determine BK build result based on amount of failed tests and their severity
# send notification to slack, then `exit` the build
determine_build_result() {
    if [[ "$MINORS" > 9 || "$NORMALS" > 0 || "$CRITICALS" > 0 || "$BLOCKERS" > 0 ]]; then
    	send_slack_notification 1
    	exit 1
    elif [[ "$MINORS" < 10 && "$NORMALS" = 0 && "$CRITICALS" = 0 && "$BLOCKERS" = 0 ]]; then
        send_slack_notification 0
    	exit 0
    fi
}

send_slack_notification() {
RESULTS="
Total Tests: $TOTAL_TESTS
Broken: $BROKENS"

FAILURES="
Blocker: $BLOCKERS
Critical: $CRITICALS
Normal: $NORMALS
Minor: $MINORS"

DATE="$(cat allure-report/time.txt)"

TEXT="<http://10.240.0.32:8080/$DATE/#/|View Report>"

    if [ "$1" = 0 ]; then
        COLOR="good"
        PRETEXT=":thumbsup: Tests Passed!"
    elif [ "$1" = 1 ]; then
        COLOR="#B94A48"
        PRETEXT=":thumbsdown: Tests Failed!"
    fi

PAYLOAD="$(jq -n --arg a "$PRETEXT" \
     --arg b "$COLOR" \
     --arg c "$TEXT" \
     --arg d "$RESULTS"	\
     --arg e "$FAILURES"	\
     '{ "attachments": [{ "pretext": $a, "color": $b, "text": $c, "fields": [{"title": "Results:", "value": $d}, {"title": "Failures:", "value": $e}], "mrkdwn_in": ["text", "pretext", "fields"] }], "mrkdwn": true }'
)"

    curl -X POST --data-urlencode "payload=$PAYLOAD" $HOOK
}

count_failures
determine_build_result