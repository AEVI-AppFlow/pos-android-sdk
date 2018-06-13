# AEVI POS Flow SDK

**Disclaimer:** Note that this SDK is currently in **beta** and is subject to breaking API changes before the first release.

The AEVI POS Flow SDK enables developers to build applications for all stages of the point of sale journey.

Please see the [Wiki](https://github.com/Aevi-UK/pos-flow-sdk/wiki) for further information.

## Prerequisites

In order to test the integration with POS Flow from your application, you will need the AEVI `Flow Processing Service` installed
on your device. Please contact AEVI to discuss access to this application.

## Integrate

In your root project `build.gradle` file, you'll need to include our public bintray repository in the repositories section.

```
repositories {
    maven {
        url "http://dl.bintray.com/aevi/aevi-uk"
    }
}
```

And then for each project/module where you want to integrate with the APIs

### Payment Initiation API
```
implementation 'com.aevi.sdk.pos.flow:payment-initiation-api:1.0.0-beta9'
```

### Flow Service API
```
implementation 'com.aevi.sdk.pos.flow:flow-service-api:1.0.0-beta9'
```

### Payment Service API
```
implementation 'com.aevi.sdk.pos.flow:payment-service-api:1.0.0-beta9'
```

## Sample usage

There are three code samples in this repository to illustrate the use of each API. Please see
- `PaymentInitationSample` for an example of how to build an application that initiates payments via the `Payment Initation API`
- `FlowServiceSample` for an example of how to build an application that integrates with `Flow Service API`
- `PaymentServiceSample` for an example of how to build an application that integrates with `Payment Service API`

These samples can also be used to test the full POS Flow integration.
If you are building a POS app, you will want to use the FlowServiceSample and PaymentServiceSample to perform end to end testing.
In a similar manner, if you are developing a flow app or a payment app, you will want to use the PaymentInitiationSample to initiate payments.

## Documentation

* [Wiki](https://github.com/Aevi-UK/pos-flow-sdk/wiki)
* [Javadocs](https://github.com/Aevi-UK/pos-flow-sdk/wiki/javadocs)

# Bugs and Feedback

For bugs, feature requests and discussion please use [GitHub Issues](https://github.com/Aevi-UK/pos-flow-sdk/issues)

# LICENSE

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