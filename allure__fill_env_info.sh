#!/bin/bash

if [ "$BROWSER" == "chrome" ]; then
	echo Browser=`google-chrome -version` >> target/allure-results/environment.properties
elif [ "$BROWSER" == "firefox" ]; then
	echo Browser=`firefox -version` >> target/allure-results/environment.properties
else
	echo Browser="Browser=Couldn't identify browser" >> target/allure-results/environment.properties
fi

echo Environment=$API_URL >> target/allure-results/environment.properties

echo Test Suite=$SUITE >> target/allure-results/environment.properties