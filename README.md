# FoxCommerce UI Automation

The FoxCommerce E2E UI automation test framework. Used for automating UI test scenarios across Store Admin and Storefront.

## Installation

#### Ubuntu
1. Install Java:

	```
	sudo apt-get install default-jdk
	```

2. Install SBT:

	```
	echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
	sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
	sudo apt-get update
	sudo apt-get install sbt
	```

3. Install Scala:

	```
	sudo apt-get install scala
	```

4. Install Chrome:

	Go download it!

5. Get Chromedriver:

	Get the latest version at https://sites.google.com/a/chromium.org/chromedriver/downloads

6. Run configuration script:

	```
	make configure-linux
	```

#### MacOS
1. Install Java:

	```
	brew cask install java
	```

2. Install SBT:

	```
	brew install sbt
	```

3. Install Scala:

	```
	brew install scala
	```

4. Install Chrome:

	You already have it :)

5. Get Chromedriver:

	Get the latest version at https://sites.google.com/a/chromium.org/chromedriver/downloads

6. Run configuration script:

	```
	make configure
	```

## Running Tests

#### Locally

Environment against which tests should run are configured through env variables:
With the following values tests will run in Chrome, against FoxComm stage, with TPG storefront.

```
export ENV=stage
export API_URL__TESTS=https://stage.foxcommerce.com/api
export ASHES_URL=https://stage.foxcommerce.com/admin
export STOREFRONT_URL=https://stage.foxcommerce.com/perfect-gourmet
export STOREFRONT=perfect-gourmet
export SUITE=local
export BROWSER=chrome
export ALLURE_CONFIG=/home/userName/.../ui-automation/allure.properties
```
Once variables are configured, you can run them launching the following .sh script from project root dir:

```
	./test_run.sh {X server} {test suite}
```

Parameters:
* X server. You can run them either in normal browser `b`, using Xvfb `x` (virtual graphical server) which.
	I'd suggest using Xvfb - it grants faster runtime and browser won't bother popping up on each failure.
* Test suites available for usage:
	- `regression` -- full test run
	- `ashes`
	- `storefront-tpg`
	- `products` -- anything related to products and SKUs. Storefront part is run against TPG storefront.

Example:

```
	./test_run.sh x regression
```


Once test run is complete, report will be generated and opened in default browser.

To open report again, run this from dir that contains `allure-report` dir:

```
	allure report open
```

#### Buildkite CI
* To configure the environment against which to run tests, change `$API_URL` value to what you need.
* Builds are scheduled on 7 p.m. PST
* Scheduled build is run against `stage`

## Reporting

Reports generated from CI test runs are saved here: http://automation.foxcommerce.com/

Each environment can have up to 5 reports latest reports.

## Stack

* Main Language: Java
* Framework: TestNG, Selenide
* Build Tool: SBT
* Reporting: Allure

## Project Structure


```

                                                                         [TestNG listener]
                                         ┌──────────────┐ implements      ┌─────────────┐
      [wrappers, helpers, screenshoter]  │  ConciseAPI  ├────────────────►│  IHookable  │
                                         └──────┬────┬──┘                 └─────────────┘
                                                │    │
                                                │    │
                                                │    └────────────────────────┐
                                        extends │                             │ extends
                                                ▼                             ▼
                                         ┌─────────────┐              ┌──────────────┐
            [all kind of configurations] │  BaseTest   │              │   BasePage   │
                                         └──────┬──────┘              └──┬─────────┬─┘
        ┌─────────────┐                         │                        │         │
        │ Phoenix API │◄─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐   │                        │         │
        └─────────────┘                     ¦   │                        │         │
                                            ¦   │ extends        extends │         │ extends
                                            ▼   ▼                        ▼         ▼
                                        ┌──────────────┐      ┌─────────────┐    ┌─────────────┐
                                        │ DataProvider │      │  PageClass  │    │  PageClass  │
          [fulfills test preconditions] └─────┬────────┘      └─┬───────────┘    └───────┬─────┘
                                          ▲   │                 ¦                        ¦
                  ┌──────────────┐        │   │                 ¦                        ¦
                  │ bin/payloads ├────────┘   │                 ¦                        ¦
                  └──────────────┘            │                 ¦                        ¦
                                              ├─────────┐       ¦                        ¦
                                              │ extends │       ¦                        ¦
                          ┌─                  │         ▼       ▼                        ¦
                          │                   │      ┌─────────────┐                     ¦
                          │                   │      │  TestClass  │                     ¦
        ┌────────────┐    │           extends │      └─────────────┘                     ¦
        │ testng.xml │◄───┤                   ▼                                          ¦
        └────────────┘    │         ┌─────────────┐                                      ¦
         [test suite]     │         │  TestClass  │◄─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┘
                          │         └─────────────┘            ┌                            ┐
                          └─                                   │ page object                │
                                                               │ is created in testMethod() │
                                                               │ inside of a TestClass      │
                                                               └                            ┘

```
