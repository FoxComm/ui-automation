#!/bin/bash
# Determines if test run was successful or not based on results of tests with severity.

MINORS=0
NORMALS=0
CRITICALS=0
BLOCKERS=0

count_failures() {
    # get "testCases" json array from graph.json, then get its length

    tests_amount=$(cat allure-report/data/graph.json | jq '.testCases' | jq '. | length')

    # iterate through testCases json array
    # get $severity and $status for each test
    # if status equals "FAILED" then increase amount of failed tests with certain $severity

    for (( i=0; i<$tests_amount; i++ )); do

        severity=($(cat allure-report/data/graph.json | jq ".testCases[$i].severity"))
        status=($(cat allure-report/data/graph.json | jq ".testCases[$i].status"))

        if [[ "$status" == "\"FAILED\"" ]]; then

            case "$severity" in
                "\"MINOR\"") MINORS=$((MINORS+1)) ;;
                "\"NORMAL\"") NORMALS=$((NORMALS+1)) ;;
                "\"CRITICAL\"") CRITICALS=$((CRITICALS+1)) ;;
                "\"BLOCKER\"") BLOCKERS=$((BLOCKERS+1)) ;;
            esac
        fi

    done
}

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
    if [ "$1" = 0 ]; then
        PAYLOAD='payload={ "attachments": [{"pretext": "Tests Passed", "color": "good", "text": "<http://10.240.0.32:8080/#/|View Report>"}] }'
    elif [ "$1" = 1 ]; then
        PAYLOAD='payload={ "attachments": [{"pretext": "Tests Failed", "color": "#B94A48", "text": "<http://10.240.0.32:8080/#/|View Report>"}] }'
    fi

    curl -X POST --data-urlencode "$PAYLOAD" $HOOK
}

count_failures
determine_build_result