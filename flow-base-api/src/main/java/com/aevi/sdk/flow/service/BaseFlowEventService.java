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

import android.support.annotation.NonNull;

import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.flow.stage.StatusUpdateModel;

/**
 * Base service for handling flow event requests defined by a event type and associated bespoke data.
 */
public abstract class BaseFlowEventService extends BaseStatusUpdateService {

    @Override
    protected void processRequest(@NonNull StatusUpdateModel stageModel) {
        if (stageModel.getRequest().getRequestData().hasData("flowEvent")) {
            FlowEvent fe = stageModel.getRequest().getRequestData().getValue("flowEvent", FlowEvent.class);
            if (fe != null) {
                processFlowEvent(fe);
            }
        }
    }

    /**
     * Handle the flow event
     *
     * @param flowEvent The flow event
     */
    protected abstract void processFlowEvent(@NonNull FlowEvent flowEvent);
}
