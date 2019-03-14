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

package com.aevi.sdk.flow.stage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.flow.model.InternalData;
import io.reactivex.Observable;

/**
 * In order to avoid having mixed service/activity logic in the stage models, this base class provides a foundation for concrete activity and service
 * implementations supporting stage models being initiated via either component. This allows for isolation of activity or service specific code in
 * each relevant delegate.
 */
abstract class AndroidComponentDelegate {

    private final InternalData senderInternalData;

    AndroidComponentDelegate(InternalData senderInternalData) {
        this.senderInternalData = senderInternalData;
    }

    /**
     * Returns the InternalData of the client application that sent this request
     *
     * @return A {@link InternalData} object
     */
    @Nullable
    public InternalData getSenderInternalData() {
        return senderInternalData;
    }

    /**
     * Send message to client.
     *
     * @param appMessage message to send
     */
    abstract void sendMessage(AppMessage appMessage);

    /**
     * Get the stream of flow service events sent by FPS.
     *
     * @return Stream of flow service events
     */
    abstract Observable<FlowEvent> getFlowServiceEvents();

    /**
     * Process request in an activity.
     *
     * @param context        Context
     * @param activityIntent The intent
     * @param requestJson    The request data
     * @return An instance of ObservableActivityHelper
     */
    abstract ObservableActivityHelper<AppMessage> processInActivity(Context context, Intent activityIntent, String requestJson);
}
