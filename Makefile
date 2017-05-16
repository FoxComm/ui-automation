configure:
	mkdir -p ./bin
	cd ./bin && wget http://chromedriver.storage.googleapis.com/2.27/chromedriver_mac64.zip
	cd ./bin && unzip chromedriver_mac64.zip

configure-linux:
	mkdir -p ./bin
	cd ./bin && wget http://chromedriver.storage.googleapis.com/2.27/chromedriver_linux64.zip
	cd ./bin && unzip chromedriver_linux64.zip

build:
	sbt compile test:compile

test:
	bash define_test_suite.sh
	sbt "test-only TestRunner"

report:
	bash allure__fill_env_info.sh
	allure generate target/allure-results

result:
    source define_test_run_results.sh