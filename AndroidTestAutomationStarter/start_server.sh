#!/bin/bash


./gradlew clean fatJar

java -jar ./kotlinapi/build/libs/kotlinapi.jar
