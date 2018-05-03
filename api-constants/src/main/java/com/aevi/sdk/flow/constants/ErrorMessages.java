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
