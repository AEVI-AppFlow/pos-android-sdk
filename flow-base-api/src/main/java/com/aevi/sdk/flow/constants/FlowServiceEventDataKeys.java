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

import com.aevi.sdk.flow.model.FlowEvent;

/**
 * Data keys for flow service event data held in the {@link FlowEvent#getData()} structure.
 */
public interface FlowServiceEventDataKeys {

    /**
     * In the case of a {@link FlowServiceEventTypes#RESPONSE_REJECTED} event, the reason for the rejection can be found via this data key.
     *
     * The reason is represented as a String.
     */
    String REJECTED_REASON = "rejectedReason";

}
