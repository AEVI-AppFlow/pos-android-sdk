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

package com.aevi.sdk.pos.flow.flowservicesample.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.pos.flow.flowservicesample.settings.ServiceStateHandler;
import com.aevi.sdk.pos.flow.service.ActivityProxyService;

public class FlowActivityProxyService extends ActivityProxyService {

    @Override
    protected void processRequest(@NonNull ClientCommunicator clientCommunicator, @NonNull String request, @NonNull String flowStage) {
        if (ServiceStateHandler.isStageEnabled(this, flowStage)) {
            super.processRequest(clientCommunicator, request, flowStage);
        } else {
            Log.i(FlowActivityProxyService.class.getSimpleName(), "Current stage not enabled in flow service sample, bypassing..");
            clientCommunicator.finishWithNoResponse();
        }
    }
}
