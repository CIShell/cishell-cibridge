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

## Prebuild containers

There are two pre-built containers that are deployed with every commit to master or develop. You can download them here:

- [CIShell Container](https://cishell.jfrog.io/cishell/cibridge/latest/cishell-cibridge-container.zip) - An 'empty' shell that has all the cishell dependencies and one test algorithm installed.
- [Sci2 Container](https://cishell.jfrog.io/cishell/cibridge/sci2/latest/sci2-cibridge-container.zip) -A CIShell/CIBridge container with >100 algorithms from Sci2.

## Build Infrastructure

Build infrastructure for CIShell and Sci2 generously provided by the following open source patrons:

[![GitHub](https://cishell.github.io/images/GitHub_Logo.png)](https://github.com/CIShell/)
[![Travis CI](https://cishell.github.io/images/TravisCI-Full-Color.png)](https://travis-ci.com/CIShell/)
[![JFrog Artifactory](https://cishell.github.io/images/Powered-by-artifactory_03.png)](https://cishell.jfrog.io)
