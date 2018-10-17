# AEVI AppFlow - POS Android SDK

AEVI AppFlow enables applications to be called in different stages of a _flow_ that has been initiated by a client application.

The POS Android SDK for AEVI AppFlow applies this solution to Android "SmartPOS" devices, allowing a POS application to initiate flows where value added
services such as loyalty, split bill, receipt delivery, are called as part of the payment.

The SDK consists of two APIs - one for POS applications to initiate flows and one for app

Please see the [Wiki](https://github.com/AEVI-AppFlow/pos-android-sdk/wiki) for further information.

[ ![Download](https://api.bintray.com/packages/aevi/aevi-uk/pos-flow-sdk/images/download.svg) ](https://bintray.com/aevi/aevi-uk/pos-flow-sdk/_latestVersion)

## Prerequisites

In order to test the integration with this SDK from your application, you will need the AEVI `FPS` (`Flow Processing Service`) installed
on your device. Please download FPS and associated binaries from [here](https://github.com/AEVI-AppFlow/pos-android-sdk/wiki/fps-installation) for development and testing purposes.

If you require other variants of FPS, please get in touch with us at info@aevi.com.

## API Feature Support

Some of the features provided by this API are dependent on them being allowed and enabled in the `Flow Processing Service`.

Whether or not a feature is allowed is down to the acquirer and/or merchant configuration.
You can check whether a feature is enabled or not via the `FlowClient.getSystemSettings()` call.

See `SystemSettingsFragment` in `PaymentInitiationSample` for examples.

Here is a table outlining the features that are down to configuration.

| Feature | System settings |
| ------- | ------------------- |
| Multi-device | getFpsSettings().isMultiDevice() |
| Split | isSplitEnabled() |
| Currency change | getFpsSettings().isCurrencyChangeAllowed() |

## Integrate

In your root project `build.gradle` file, you'll need to include our public bintray repository in the repositories section.

```
repositories {
    maven {
        url "http://dl.bintray.com/aevi/aevi-uk"
    }
}
```

These APIs require that your application is compiled with Java 8. Ensure that your application build.gradle `android` DSL contains the following.
```
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
```

And then for each project/module where you want to integrate with the APIs (replace <version> with latest release)

### Payment Initiation API
```
implementation 'com.aevi.sdk.pos.flow:payment-initiation-api:<version>'
```

### Flow Service API
```
implementation 'com.aevi.sdk.pos.flow:flow-service-api:<version>'
```

### Payment Service API
```
implementation 'com.aevi.sdk.pos.flow:payment-service-api:<version>'
```

### Constants

API constants are now stored in a separate repo here [https://github.com/Aevi-UK/pos-flow-sdk-constants](https://github.com/Aevi-UK/pos-flow-sdk-constants)

To include these in your project use
```
implementation `com.aevi.sdk.flow:api-constants:<version>'
```

## Sample usage

There are three code samples in this repository to illustrate the use of each API. Please see
- `PaymentInitationSample` for an example of how to build an application that initiates payments via the `Payment Initation API`
- `FlowServiceSample` for an example of how to build an application that integrates with `Flow Service API`
- `PaymentServiceSample` for an example of how to build an application that integrates with `Payment Service API`

These samples can also be used to test the full SDK integration.
If you are building a POS app, you will want to use the FlowServiceSample and PaymentServiceSample to perform end to end testing.
In a similar manner, if you are developing a flow app or a payment app, you will want to use the PaymentInitiationSample to initiate payments.

## Build Environment

### Minimum versions

The minimum versions for importing / building this API are
- Gradle v4.4 (v4.8+ recommended)
- Android Gradle Plugin v3.1.3
- Android Studio v3.1.3

### Known Issues

The API and applications use Gradle v4.8 for building. Due to a bug in this version building in Android Studio (v3.1.3 at time of writing) will cause the following
error:

```text
Configuration on demand is not supported by the current version of the Android Gradle plugin since you are using Gradle version 4.6 or above. Suggestion: disable configuration on demand by setting org.gradle.configureondemand=false in your gradle.properties file or use a Gradle version less than 4.6.
```

In order to disable configuration on demand in Android Studio it must be configured in the settings for Android Studio not the
`gradle.properties` file as described above. You can disable this setting by navigating to

```text
Settings - Build, Execution, Deployment - Compiler - Configure on demand
```

and deselecting the check box.

## Documentation

* [Wiki](https://github.com/AEVI-AppFlow/pos-android-sdk/wiki)
* [Javadocs](https://github.com/AEVI-AppFlow/pos-android-sdk/wiki/javadocs)

## Bugs and Feedback

For bugs, feature requests and discussion please use [GitHub Issues](https://github.com/AEVI-AppFlow/pos-android-sdk/issues)

## LICENSE

Copyright 2018 AEVI International GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
