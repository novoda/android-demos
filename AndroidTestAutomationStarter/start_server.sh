#!/bin/bash

cd kotlinapi

./gradlew fatJar

java -jar ./build/libs/kotlinapi.jar
