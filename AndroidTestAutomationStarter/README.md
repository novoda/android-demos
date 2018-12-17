## Overview

A sample retail store app that showcases current testing methodologies for Android. It comes with a simple Heroku server that can be run locally.

The aim is to provide a hands-on introduction to those new to test engineering and a challenge for those who want to further improve the current implementation.


## Getting started

Before launching the app you'll need to start the server.

execute `start_server.sh`


You can check if the server is running by going to http://localhost:4567/ where you should see **Server is running**

Now all you need to do is run the app from Android Studio!

## Tests

We have tests to validate both the UI and the JSON Schema. The first are in `src/androidTest/java/com/novoda/androidstoreexample` and the latter in `src/test/java/com/novoda/androidstoreexample`

To find out more about our approach to UI tests, see  https://docs.google.com/document/d/1S6f3tVwB0se1Xe3qc1Cv4I5ZVch-7lKJiuVgW_ktLkY/edit (to be replaced with wiki pages)

## Current limitations

* The content from the server can only be viewed on an emulator.

* Right now we only have data for the "Hats" section.

* The shopping basket is still work in progress.

