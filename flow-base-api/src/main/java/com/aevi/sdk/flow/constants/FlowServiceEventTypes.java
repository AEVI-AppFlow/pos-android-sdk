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

/**
 * Event types that may be sent to a flow service.
 *
 * Note that these com.aevi.sdk.pos.flow.model.events are only propagated via a stage model initialised from a service. It is up to the service implementation to proxy them
 * to any activity if one has been started.
 */
public interface FlowServiceEventTypes {

    /**
     * If a flow service activity is no longer in the background for some reason (such as home being pressed), a merchant can choose to resume
     * the flow service user interface from the AppFlow controls. In this case, this event will be sent and the flow service can resume or restart
     * the user interface as appropriate to allow the merchant to start over or continue where she left off.
     */
    String RESUME_USER_INTERFACE = "resumeUserInterface";

    /**
     * Sent from the flow processing service when the flow service has not sent a response within the timeout limit or
     * something has gone wrong and the flow must progress. This event is purely intended to let the flow service (if still active)
     * know that it should stop any processing and close down any user interface immediately. No response will be accepted at this stage.
     */
    String FINISH_IMMEDIATELY = "finishImmediately";

    /**
     * Sent from the flow processing service to indicate that the response sent from the flow service was accepted and any changes resulting from
     * that response have been applied to the flow successfully. Note that even if you send an empty response or call a skip function, this will
     * be sent back to confirm your choice.
     */
    String RESPONSE_ACCEPTED = "responseAccepted";

    /**
     * Sent from the flow processing service to indicate that the response sent from the flow service was rejected, meaning that any changes from this
     * have NOT been applied.
     *
     * See {@link FlowServiceEventDataKeys#REJECTED_REASON} for details on why it was rejected.
     */
    String RESPONSE_REJECTED = "responseRejected";

}
