# Demo WearBuildConfig

WearBuildConfig demonstrates how to integrate Android Wear with a real-world mobile project with the following requirements:


* multi-dimensions flavours (`development` and `production` environments, with or without Play Services)
* `debug`, `release` and `QA` builds
* different signing keys for different build types

The Wear APK is bundled in the mobile main APK assets only for `release` and `QA` builds, and only for builds including Play Services
