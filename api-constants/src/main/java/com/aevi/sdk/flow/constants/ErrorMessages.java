/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.aevi.sdk.flow.constants;

public interface ErrorMessages {

    String MERCHANT_CANCELLED = "Merchant cancelled transaction";
    String UNEXPECTED_FAILURE = "Transaction failed unexpectedly";
    String DEVICE_CHOSEN_NOT_CONNECTED = "The device chosen for application execution is not currently connected";

    String NO_ELIGIBLE_APP = "No application eligible to handle the request";
    String APP_TIMED_OUT = "Application timed out";

    String NO_ELIGIBLE_PAYMENT_APP = "No payment app eligible to handle the payment";
    String PAYMENT_APP_TIMED_OUT = "Payment app timed out";
    String PAYMENT_APP_NOT_INSTALLED = "Payment app not installed on this device";

}
