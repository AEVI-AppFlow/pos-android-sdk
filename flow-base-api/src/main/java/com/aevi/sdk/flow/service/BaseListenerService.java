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

import com.aevi.android.rxmessenger.service.AbstractMessengerService;
import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.BaseModel;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.model.Response;

import static com.aevi.sdk.flow.BaseApiClient.FLOW_PROCESSING_SERVICE;
import static com.aevi.sdk.flow.constants.AppMessageTypes.REQUEST_ACK_MESSAGE;
import static com.aevi.sdk.flow.service.BaseApiService.checkVersions;

/**
 * Base service used for notifying clients of the final response for any transaction.
 *
 * This class should not be used directly instead choose one of the child classes
 * e.g. for generic responses use {@link BaseResponseListenerService}.
 */
public abstract class BaseListenerService<RESPONSE extends BaseModel> extends AbstractMessengerService {

    private final Class<RESPONSE> responseClass;
    private final InternalData internalData;

    protected BaseListenerService(Class<RESPONSE> responseClass, String apiVersion) {
        this.responseClass = responseClass;
        internalData = new InternalData(apiVersion);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void handleRequest(String clientMessageId, String message, String packageName) {
        sendAck(clientMessageId);
        sendEndStreamMessageToClient(clientMessageId);
        if (FLOW_PROCESSING_SERVICE.equals(packageName)) {
            AppMessage appMessage = AppMessage.fromJson(message);
            checkVersions(appMessage, internalData);

            if (AppMessageTypes.RESPONSE_MESSAGE.equals(appMessage.getMessageType())) {
                Response response = Response.fromJson(appMessage.getMessageData());
                RESPONSE unwrapped = null;
                if (responseClass.equals(Response.class)) {
                    unwrapped = (RESPONSE) response;
                } else {
                    unwrapped = response.getResponseData().getValue(AppMessageTypes.PAYMENT_MESSAGE, responseClass);
                }

                if (unwrapped != null) {
                    notifyResponse(unwrapped);
                }
            }
        }
    }

    private void sendAck(String clientMessageId) {
        AppMessage appMessage = new AppMessage(REQUEST_ACK_MESSAGE, internalData);
        sendMessageToClient(clientMessageId, appMessage.toJson());
    }

    /**
     * This method will be called with the appropriate response for clients that initiated the matching request or flow services that have
     * processed the request in some way and require to be notified of the final response
     *
     * @param response The final response sent after completion of a flow
     */
    protected abstract void notifyResponse(RESPONSE response);
}
