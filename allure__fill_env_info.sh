#!/bin/bash

if [ "$BROWSER" == "chrome" ]; then
	echo BROWSER=`google-chrome -version` >> target/allure-results/environment.properties
elif [ "$BROWSER" == "firefox" ]; then
	echo BROWSER=`firefox -version` >> target/allure-results/environment.properties
else
	echo BROWSER="Browser=Couldn't identify browser" >> target/allure-results/environment.properties
fi

echo ENVIRONMENT=$ENV >> target/allure-results/environment.properties
echo API_URL__TESTS=$API_URL__TESTS >> target/allure-results/environment.properties
echo ASHES_URL=$ASHES_URL >> target/allure-results/environment.properties
echo STOREFRONT_URL=$STOREFRONT_URL >> target/allure-results/environment.properties
echo STOREFRONT=$STOREFRONT >> target/allure-results/environment.properties
echo TEST_SUITE=$SUITE >> target/allure-results/environment.properties