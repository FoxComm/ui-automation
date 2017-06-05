configure:
	mkdir -p ./bin
	cd ./bin && wget http://chromedriver.storage.googleapis.com/2.27/chromedriver_mac64.zip
	cd ./bin && unzip chromedriver_mac64.zip

configure-linux:
	mkdir -p ./bin
	cd ./bin && wget http://chromedriver.storage.googleapis.com/2.27/chromedriver_linux64.zip
	cd ./bin && unzip chromedriver_linux64.zip

build:
	mvn clean

test:
	bash define_test_suite.sh
	mvn test

report:
	bash allure__fill_env_info.sh
	allure generate target/allure-results