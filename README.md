# FoxComm UI Automation

The FoxCommerce E2E UI automation test suite.

## Getting Started

### 1A) Install Prerequisites (OS X)

- Java JDK 1.8

    $ brew cask install java

- SBT

    $ brew install sbt

- Scala

    $ brew install scala

### 1B) Install Prerequisites (Ubuntu)

Coming soon...

### 2) Compile

    $ sbt compile test:compile

### 3) Run Tests

    $ sbt "test-only TestRunner"
