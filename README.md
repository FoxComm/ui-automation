# FoxComm UI Automation

The FoxCommerce E2E UI automation test suite.

## Getting Started

**1A) Install Prerequisites (OS X)**

- Java JDK 1.8

        brew cask install java

- SBT

        brew install sbt

- Scala

        brew install scala

- Chrome

    You already have it. :)

**1B) Install Prerequisites (Ubuntu 16.04)**

- Java JDK 1.8

        sudo apt-get install default-jdk

- SBT

        echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
        sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
        sudo apt-get update
        sudo apt-get install sbt

- Scala

        sudo apt-get install scala

- Chrome

    Go download it!

**2A) Configure the Application (OS X)**

    make configure

**2B) Configure the Application (Ubuntu 16.04)**

    make configure-linux

**3) Compile**

    make build

**4) Run Tests**

    make test
