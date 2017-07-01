#!/bin/bash

rm_testng_xml() {
    if [ -e testng.xml ]; then
        rm testng.xml
    fi
}

case "$SUITE" in
    "regression")
    echo "Executing Regression Test Suite"
    cp test-suites/regression.xml regression.xml
    rm_testng_xml
    mv regression.xml testng.xml ;;

	"ashes")
	echo "Executing Ashes Test Suite"
	cp test-suites/ashes.xml ashes.xml
	rm_testng_xml
	mv ashes.xml testng.xml ;;

	"products")
	echo "Executing Products Test Suite"
	cp test-suites/products.xml products.xml
	rm_testng_xml
	mv products.xml testng.xml ;;

	"storefront-tpg")
	echo "Executing TPG Storefront Test Suite"
	cp test-suites/storefront-tpg.xml storefront-tpg.xml
	rm_testng_xml
	mv storefront-tpg.xml testng.xml ;;

	"test")
	echo "Executing Testing Test Suite"
	cp test-suites/test.xml test.xml
	rm_testng_xml
	mv test.xml testng.xml ;;

    "local")
	echo "Running tests on local"
	echo "Keeping current testng.xml unchanged" ;;
esac