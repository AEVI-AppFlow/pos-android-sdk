package com.aevi.sdk.flow.model;

/**
 * Collection of message types used for communication between PCS and various apps.
 */
public interface AppMessageTypes {

    String REQUEST_MESSAGE = "request";
    String RESPONSE_MESSAGE = "response";
    String FAILURE_MESSAGE = "failure";
    String FORCE_FINISH_MESSAGE = "forceFinish";
    String REQUEST_ACK_MESSAGE = "requestAck";
}
