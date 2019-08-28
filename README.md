# AEVI AppFlow - POS Android SDK

**Note** - AppFlow v2.1.1 is now released!

AEVI AppFlow is a solution that enables a client application to initiate a _flow_ that consists of one to many _stages_ in which any number of applications may be called.
A set of input and output data structures are defined for each stage, allowing applications to view the latest state and/or augment it.

The point of sale (POS) Android SDK for AEVI AppFlow applies this solution to Android "SmartPOS" devices, allowing a POS application to initiate various
flows for common operations such as payments, refunds, voids etc. In addition to calling standard payment applications, any number of value added services can be called during the flow,
such as loyalty, split bill, receipt delivery and much more. AppFlow for POS is highly configurable and flexible, allowing dynamic configuration of flows and input / output data.

The SDK consists of two APIs - one for client/POS applications to initiate flows and one for value added services and payment applications to integrate into those flows.

Please see the [Documentation](https://aevi-appflow.github.io/pos-android-sdk/) for detailed information of how AppFlow works and development guidelines.

## Prerequisites

In order to test AppFlow and/or integrate with it, you will need to at a minimum install these two AEVI provided applications;
- AEVI Flow Processing Service (FPS), which implements the APIs and executes the flows
- AEVI AppFlow Configuration App, which provides the flows and other settings for AppFlow tailored for developers

Please download the latest developer bundle from [here](https://aevi-appflow.github.io/pos-android-sdk/downloads/), which contains these applications as well as the latest samples.

## Integrate

AppFlow and its dependencies are as of `v2.1.0` published to the `jcenter()` repository which is used by default by Android Gradle project / Android Studio.

If you are using an earlier version or having issues getting this to work, you can also add our bintray repository directly as per below. 

In your root project `build.gradle` file, add

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

Use the latest version defined below.

[ ![Download](https://api.bintray.com/packages/aevi/aevi-uk/appflow-sdk/images/download.svg) ](https://bintray.com/aevi/aevi-uk/appflow-sdk/_latestVersion)

### Payment Initiation API

For client/POS applications to initiate flows.

```
implementation 'com.aevi.sdk.pos.flow:payment-initiation-api:<version>'
```

### Payment Flow Service API

For value added services and payment applications to integrate into flows.

```
implementation 'com.aevi.sdk.pos.flow:payment-flow-service-api:<version>'
```

### API Constants

The AppFlow APIs themselves are designed to facilitate communication between applications via a defined set of data structures. They are however decoupled from what the _values_ or _content_
of these data structures are, in order to keep them as flexible and configurable as possible. Instead, the data values are defined in the docs and provided as constants via a separate library.

This library is stored in a separate repo [here](https://github.com/AEVI-AppFlow/api-constants). You can find the latest version details and view the defined data constants there.

To include these in your project use
```
implementation `com.aevi.sdk.flow:api-constants:<version>'
```

## API Samples

There are three code samples in this repository to illustrate the use of the APIs. Please see
- `PaymentInitiationSample` for an example of how to build an application that initiates payments via the `Payment Initiation API`
- `FlowServiceSample` for an example of how to build a value added service that integrates with `Payment Flow Service API`
- `PaymentServiceSample` for an example of how to build a payment application that integrates with `Payment Flow Service API`

In addition to illustrating how the APIs can be used, these samples can also be used to test your own application.
If you are building a POS app, you will want to use the `FlowServiceSample` and `PaymentServiceSample` to perform testing with apps in the flow.
In a similar manner, if you are developing a flow app or a payment app, you will want to use the `PaymentInitiationSample` to initiate payments.

You can of course modify these samples to suit your use cases and scenarios.

## API Feature Support

Some of the features provided by this API are configurable by AEVI and/or the acquirer.

The `PaymentSettings` model provides access to relevant settings and can be retrieved via `PaymentClient.getPaymentSettings()`.
See `FpsSettings` for details on all the parameters.

You can check what the settings are via the `System overview` screen in the `Payment Initiation Sample`.

## Build Environment

### Minimum versions

This project is using the Gradle wrapper and we recommend using Android Studio v3.3.2 or higher.

## Documentation

* [Documentation](https://aevi-appflow.github.io/pos-android-sdk/)
* [Javadocs](https://aevi-appflow.github.io/pos-android-sdk/reference/)

## Bugs and Feedback

For bugs, feature requests and questions please use [GitHub Issues](https://github.com/AEVI-AppFlow/pos-android-sdk/issues).

## Contributions

Contributions to any of our repos via pull requests are welcome. We follow the [git flow](https://nvie.com/posts/a-successful-git-branching-model/) branching model.

## LICENSE

Copyright 2019 AEVI International GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
