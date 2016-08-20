configure:
	mkdir -p ./bin
	cd ./bin && wget http://chromedriver.storage.googleapis.com/2.23/chromedriver_mac64.zip
	cd ./bin && unzip chromedriver_mac64.zip

build:
	sbt compile test:compile

test:
	sbt "test-only TestRunner"
