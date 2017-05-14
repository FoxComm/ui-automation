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
elif [ "$SUITE" == "ashes" ]; then
    cp test-suits/ashes.xml ashes.xml && rm testng.xml && mv ashes.xml testng.xml
	echo "Executing Ashes Test Suite"
elif [ "$SUITE" == "storefront-tpg" ]; then
    cp test-suits/storefront-tpg.xml storefront-tpg.xml && rm testng.xml && mv storefront-tpg.xml testng.xml
    echo "Executing TPG Storefront Test Suite"
else
	echo "ERROR: Unknown test suite is defined"
	echo "Fallback to current testng.xml"
fi
