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
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.flow.model.AppMessage;
import io.reactivex.Observable;

/**
 * Base class for delegates that are designed specifically for a type of Android component.
 */
abstract class AndroidComponentDelegate {

    /**
     * Send message to client.
     *
     * @param appMessage message to send
     */
    abstract void sendMessage(AppMessage appMessage);

    /**
     * Get the stream of flow service messages sent by FPS.
     *
     * @return Stream of flow service messages
     */
    abstract Observable<String> getFlowServiceMessages();

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
