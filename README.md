# CIShell CIBridge

CIBridge Implementation using CIShell, see http://github.com/CIShell/cibridge/

[![Build Status](https://travis-ci.com/CIShell/cishell-cibridge.svg?branch=develop)](https://travis-ci.com/CIShell/cishell-cibridge)

## Build Instructions

This repository is built using maven. To build this repository, just run:
```
mvn clean install
```

## Running the Container

Container creates a distribution that can be run by command line like this:
```
java -jar container/target/org.cishell.container-1.0.0-*.jar container/target/plugins/
```

## Build Infrastructure

Build infrastructure for CIShell and Sci2 generously provided by the following open source patrons:

[![GitHub](https://cishell.github.io/images/GitHub_Logo.png)](https://github.com/CIShell/)
[![Travis CI](https://cishell.github.io/images/TravisCI-Full-Color.png)](https://travis-ci.com/CIShell/)
[![JFrog Artifactory](https://cishell.github.io/images/Powered-by-artifactory_03.png)](https://cishell.jfrog.io)
