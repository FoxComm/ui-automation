#!/bin/bash

case "$SUITE" in
    "smoke")
    echo "Executing Smoke Test Suite"
    cp test-suites/smoke.xml smoke.xml
    rm testng.xml
    mv smoke.xml testng.xml ;;

    "regression")
    echo "Executing Regression Test Suite"
    cp test-suites/regression.xml regression.xml
    rm testng.xml
    mv regression.xml testng.xml ;;

	"ashes")
	echo "Executing Ashes Test Suite"
	cp test-suites/ashes.xml ashes.xml
	rm testng.xml
	mv ashes.xml testng.xml ;;

	"storefront-tpg")
	echo "Executing TPG Storefront Test Suite"
	cp test-suites/storefront-tpg.xml storefront-tpg.xml
	rm testng.xml
	mv storefront-tpg.xml testng.xml ;;

	"test")
	echo "Executing Testing Test Suite"
	cp test-suites/test.xml test.xml
	rm testng.xml
	mv test.xml testng.xml ;;

    "local")
	echo "Running tests on local"
	echo "Keeping current testng.xml unchanged" ;;
esac
