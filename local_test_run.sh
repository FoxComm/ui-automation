#!/bin/bash

# parametrized local test run
#
# possible parameters:
# $1 -> launch browser either in your current X server or launch Xvfb (virtual X server) and run them in there
#       running tests using Xvfb grants better runtime, also popping up browser window won't bother you
#
# $2 -> define test suite
#        options:      regression
#                      ashes
#                      storefront-tpg
#                      products
#                      test
#
# $3 -> your su password to generate allure report

# test suite
export SUITE="$2"

# browser or Xvfb
if [ "$1" = "x" ]; then
    xvfb-run -s "-screen 0 1920x1080x24 +extension GLX +render" make test
elif [ "$1" = "b" ]; then
    make test
fi

# fill out env info, generate report and open it in default browser
./allure__fill_env_info.sh
sudo -S <<< "$3" allure generate target/allure-results
allure report open
