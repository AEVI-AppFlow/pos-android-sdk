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

public interface EventTypes {

    /**
     * When anything has changed in FPS related to settings, configurations, etc, this event will be triggered.
     * The data for the event will be populated with the relevant FPS state.
     * See {@link EventDataKeys} for breakdown of what has changed
     */
    String EVENT_INTERNAL_STATE_CHANGED = "flowStateChanged";

    /**
     * When something external has changed, like devices connected/disconnected or apps added/removed/enabled/disabled, etc
     * See {@link EventDataKeys} for breakdown of what has changed
     */
    String EVENT_EXTERNAL_STATE_CHANGED = "externalStateChanged";
}
