#!/bin/bash

if [ "$SUIT" == "smoke" ]; then
	cp test-suits/smoke.xml smoke.xml && rm testng.xml && mv smoke.xml testng.xml
	echo "Executing Smoke Test Suit"
elif [ "$SUIT" == "regression" ]; then
	cp test-suits/regression.xml regression.xml && rm testng.xml && mv regression.xml testng.xml
	echo "Executing Regression Test Suit"
elif [ "$SUIT" == "local" ]; then
	echo "Running tests on local"
	echo "Keeping currect testng.xml unchanged"
else
	echo "ERROR: Unknown test suit is defined"
	echo "Fallback to current testng.xml"
fi
