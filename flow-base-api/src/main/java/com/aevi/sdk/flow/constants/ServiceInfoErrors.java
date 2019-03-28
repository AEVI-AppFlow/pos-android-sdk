package com.aevi.sdk.flow.constants;

/**
 * Error types related to {@link com.aevi.sdk.flow.service.BaseServiceInfoProvider#onServiceInfoError(String, String)}
 */
public interface ServiceInfoErrors {

    /**
     * Indicates that the flow service did not return a service info response in time.
     *
     * The best approach to handling this is to call {@link com.aevi.sdk.flow.service.BaseServiceInfoProvider#notifyServiceInfoChange()} once all the required data
     * is cached in the application and can be returned without a need for network calls, etc.
     */
    String RETRIEVAL_TIME_OUT = "retrievalTimeOut";

    /**
     * Indicates that there were problems with how the flow service defines its stages.
     */
    String INVALID_STAGE_DEFINITIONS = "invalidStageDefinitions";

    /**
     * Indicates problems with the service info data reported from the flow service.
     */
    String INVALID_SERVICE_INFO = "invalidServiceInfo";
}
