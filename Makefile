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
	source define_test_run.sh

test:
	sbt "test-only TestRunner"