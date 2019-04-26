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
package com.aevi.sdk.flow.service;

import android.content.Context;
import android.support.annotation.NonNull;
import com.aevi.android.rxmessenger.ChannelServer;
import com.aevi.android.rxmessenger.service.AbstractChannelService;
import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.model.*;

import static com.aevi.sdk.flow.BaseApiClient.FLOW_PROCESSING_SERVICE;
import static com.aevi.sdk.flow.constants.AppMessageTypes.REQUEST_ACK_MESSAGE;
import static com.aevi.sdk.flow.constants.ErrorConstants.UNEXPECTED_ERROR;
import static com.aevi.sdk.flow.service.BaseApiService.checkVersions;

/**
 * Base service used for notifying clients of the final response for any transaction.
 *
 * This class should not be used directly instead choose one of the child classes
 * e.g. for generic responses use {@link BaseResponseListenerService}.
 */
public abstract class BaseListenerService<RESPONSE extends BaseModel> extends AbstractChannelService {

    private final Class<RESPONSE> responseClass;
    private final InternalData internalData;

    protected BaseListenerService(Class<RESPONSE> responseClass, String apiVersion) {
        this.responseClass = responseClass;
        internalData = new InternalData(apiVersion);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        internalData.setSenderPackageName(getPackageName());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final void onNewClient(final ChannelServer channelServer, final String packageName) {
        sendAck(channelServer);
        channelServer.subscribeToMessages().take(1).subscribe(message -> {
            AppMessage appMessage = AppMessage.fromJson(message);
            checkVersions(appMessage, internalData);
            channelServer.sendEndStream();
            if (FLOW_PROCESSING_SERVICE.equals(appMessage.getInternalData().getSenderPackageName())) {
                if (AppMessageTypes.RESPONSE_MESSAGE.equals(appMessage.getMessageType())) {
                    Response response = Response.fromJson(appMessage.getMessageData());
                    RESPONSE unwrapped;
                    if (responseClass.equals(Response.class)) {
                        unwrapped = (RESPONSE) response;
                    } else {
                        unwrapped = response.getResponseData().getValue(AppMessageTypes.PAYMENT_MESSAGE, responseClass);
                    }

                    if (unwrapped != null) {
                        notifyResponse(unwrapped);
                    }
                } else if (AppMessageTypes.FAILURE_MESSAGE.equals(appMessage.getMessageType())) {
                    FlowException flowException = FlowException.fromJson(appMessage.getMessageData());
                    notifyError(flowException.getErrorCode(), flowException.getErrorMessage());
                }
            }
        }, throwable -> {
            if (throwable instanceof FlowException) {
                notifyError(((FlowException) throwable).getErrorCode(), ((FlowException) throwable).getErrorMessage());
            } else {
                notifyError(UNEXPECTED_ERROR, throwable.getMessage());
            }
        });
    }

    private void sendAck(ChannelServer channelServer) {
        AppMessage appMessage = new AppMessage(REQUEST_ACK_MESSAGE, internalData);
        channelServer.send(appMessage.toJson());
    }

    /**
     * This method will be called with the appropriate response for clients that initiated the matching request or flow services that have
     * processed the request in some way and require to be notified of the final response
     *
     * @param response The final response sent after completion of a flow
     */
    protected abstract void notifyResponse(@NonNull RESPONSE response);

    /**
     * This method will be called upon unrecoverable errors in the flow.
     *
     * @param errorCode    The error code as per {@link com.aevi.sdk.flow.constants.ErrorConstants}
     * @param errorMessage Error message to further outline the problem
     */
    protected abstract void notifyError(@NonNull String errorCode, @NonNull String errorMessage);
}
