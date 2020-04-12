## \[ 🚧 Work in progress 👷‍♀️⛏👷🔧️🚧 \] Huawei HMS [![CircleCI](https://circleci.com/gh/jumaallan/huawei-hms/tree/master.svg?style=shield&circle-token=ecbe4349994c1369fa34b83a90fbc402c82176ae)](https://circleci.com/gh/jumaallan/huawei-hms/tree/master)


👀  Writing Huawei HMS App using [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/), in 100% Kotlin, using the Huawei Map Service SDK - part of Huawei Mobile Services. 

> I have committed the agconnect-services json file, but should be replaced with one generated on your side


### Background

Develop a demo app, using the Huawei Map Kit and Location Kit SDK:

* When the user opens the app, it should locate the user's current location and show on the Huawei Map.
* Mark the user's current location on the Map with a star/marker, and pop up the user's current address when they click the star/marker - show the address description not the Geocoding value. 
* User can move and click on the map, and the marker will move to the point as the user clicks - popup the address description when the user pins a point on the map.

### Background

Some screenshots to show the app flow:

<img src="https://github.com/jumaallan/huawei-hms/blob/master/screenshots/screenshot_one.jpeg" width="280"/> <img src="https://github.com/jumaallan/huawei-hms/blob/master/screenshots/screenshot_two.jpeg" width="280"/> <img src="https://github.com/jumaallan/huawei-hms/blob/master/screenshots/screenshot_three.jpeg" width="280"/> 

## Project characteristics

This project brings to table set of best practices, tools, and solutions:

* 100% [Kotlin](https://kotlinlang.org/)
*  Model-View-ViewModel architecture
* [Android Jetpack](https://developer.android.com/jetpack)
* [CI pipeline](https://circleci.com/)
* Testing
* Dependency Injection
* Material design

## Tech-stack

Min API level is set to [`19`](https://android-arsenal.com/api?level=19), so the presented approach is suitable for over
[98% of devices](https://developer.android.com/about/dashboards) running Android. This project takes advantage of many
popular libraries and tools of the Android ecosystem. Most of the libraries are in the stable version.

* Tech-stack
    * [Kotlin](https://kotlinlang.org/) + [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - perform background operations
    * [KOIN](https://insert-koin.io/) - dependency injection
    * [Retrofit](https://square.github.io/retrofit/) - networking
    * [Jetpack](https://developer.android.com/jetpack)
        * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - notify views about database change
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - perform action when lifecycle state changes
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way
    * [Coil](https://github.com/coil-kt/coil) - image loading library with Kotlin idiomatic API
    * [Stetho](http://facebook.github.io/stetho/) - application debugging tool

* Architecture
    * MVVM - application level
    * [Android Architecture components](https://developer.android.com/topic/libraries/architecture) ([ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel), [LiveData](https://developer.android.com/topic/libraries/architecture/livedata), [SafeArgs](https://developer.android.com/guide/navigation/navigation-pass-data#Safe-args) plugin)
* Tests
    * [Unit Tests](https://en.wikipedia.org/wiki/Unit_testing) ([JUnit](https://junit.org/junit4/))
    * [Mockito](https://github.com/mockito/mockito) + [Mockito-Kotlin](https://github.com/nhaarman/mockito-kotlin)
    * [Kluent](https://github.com/MarkusAmshove/Kluent)
* Gradle
    * [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html)
    * Custom tasks
    * Plugins ([Ktlint](https://github.com/JLLeitschuh/ktlint-gradle), [Detekt](https://github.com/arturbosch/detekt#with-gradle), [Versions](https://github.com/ben-manes/gradle-versions-plugin), [SafeArgs](https://developer.android.com/guide/navigation/navigation-pass-data#Safe-args))


## Getting started

There are a few ways to open this project.

### Android Studio

1. Android Studio -> File -> New -> From Version control -> Git
2. Enter `https://github.com/jumaallan/huawei-hms.git` into URL field

### Command line + Android Studio

1. Run `git clone https://github.com/jumaallan/huawei-hms.git`
2. Android Studio -> File -> Open

## License
```
MIT License

Copyright (c) 2020 Juma Allan

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
associated documentation files (the "Software"), to deal in the Software without restriction, including 
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to 
the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial 
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN 
NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
WHETHER IN AN ACTION OF  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```