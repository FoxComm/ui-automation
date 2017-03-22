#!/bin/bash

if [ "$SUITE" == "smoke" ]; then
	cp test-suits/smoke.xml smoke.xml && rm testng.xml && mv smoke.xml testng.xml
	echo "Executing Smoke Test Suite"
elif [ "$SUITE" == "regression" ]; then
	cp test-suits/regression.xml regression.xml && rm testng.xml && mv regression.xml testng.xml
	echo "Executing Regression Test Suite"
elif [ "$SUITE" == "local" ]; then
	echo "Running tests on local"
	echo "Keeping current testng.xml unchanged"
else
	echo "ERROR: Unknown test suite is defined"
	echo "Fallback to current testng.xml"
fi
