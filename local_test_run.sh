#!/bin/bash

# parametrized local test run
#
# possible parameters:
# $1 -> launch browser either in your current X server or launch Xvfb (virtual X server) and run them in there
#       running tests using Xvfb grants better runtime, also popping up browser window won't bother you
# $2 -> define test suite
# $3 -> your su password to generate allure report

# browser or Xvfb
if [ "$1" = "x" ]; then
    xvfb-run -s "-screen 0 1920x1080x24 +extension GLX +render" make test
elif [ "$1" = "b" ]; then
    make test
fi

# test suite
case "$2" in
    "regression")
    echo "Executing Regression Test Suite"
    cp test-suites/regression.xml regression.xml && rm testng.xml && mv regression.xml testng.xml ;;

	"ashes")
	echo "Executing Ashes Test Suite"
	cp test-suites/ashes.xml ashes.xml && rm testng.xml && mv ashes.xml testng.xml ;;

	"products")
	echo "Executing Products Test Suite"
	cp test-suites/products.xml products.xml && rm testng.xml && mv products.xml testng.xml ;;

	"storefront-tpg")
	echo "Executing TPG Storefront Test Suite"
	cp test-suites/storefront-tpg.xml storefront-tpg.xml && rm testng.xml && mv storefront-tpg.xml testng.xml ;;

	"test")
	echo "Executing Testing Test Suite"
	cp test-suites/test.xml test.xml && rm testng.xml && mv test.xml testng.xml ;;

    "local")
	echo "Running tests on local"
	echo "Keeping current testng.xml unchanged" ;;
esac

# fill out env info, generate report and open it in default browser
./allure__fill_env_info.sh
sudo -S <<< "$3" allure generate target/allure-results
allure report open
