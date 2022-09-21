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
package com.aevi.sdk.pos.flow.paymentinitiationsample;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseResponseListenerService;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.GenericResultActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.NotificationHelper;

import static android.content.Intent.*;

public class ResponseListenerService extends BaseResponseListenerService {

    @Override
    protected void notifyGenericResponse(Response response) {
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        notificationHelper.dismissNotification();

        Intent intent = new Intent(this, GenericResultActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_REORDER_TO_FRONT | FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(GenericResultActivity.GENERIC_RESPONSE_KEY, response.toJson());
        startActivity(intent);
    }

    @Override
    protected void notifyStatusUpdateResponse(Response response) {
        // Here we handle it the same way as a generic response for illustration, but real client apps should most likely not present UI for these
        notifyGenericResponse(response);
    }

    @Override
    protected void notifyFlowEvent(@NonNull FlowEvent flowEvent) {
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        notificationHelper.notifyEvent(flowEvent);
    }

    @Override
    protected void notifyError(@NonNull FlowException flowException) {
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        notificationHelper.dismissNotification();

        Intent intent = new Intent(this, GenericResultActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_REORDER_TO_FRONT |
                FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(GenericResultActivity.GENERIC_RESPONSE_KEY,
                new Response("N/A", false, flowException.getErrorCode() + " : " + flowException.getErrorMessage(), null).toJson());
        startActivity(intent);
    }
}
