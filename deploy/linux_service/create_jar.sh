#!/bin/bash

VERSION=$(git describe --abbrev=0 --tags)
ex -s +"g/Dversion/d" -cwq /opt/bookshelf/run.sh
echo ${VERSION}
echo "-Dapp.version=${VERSION}" >> /opt/bookshelf/run.sh
bash ./gradlew clean
bash ./gradlew vaadinBuildFrontend
bash ./gradlew bootJar
cp build/libs/book-shelf.jar /opt/bookshelf
