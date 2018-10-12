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

package com.aevi.sdk.pos.flow.flowservicesample.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aevi.sdk.pos.flow.flowservicesample.R;

import static com.aevi.sdk.flow.constants.FlowStages.*;

public class ServiceStateHandler {

    public static boolean isStageEnabled(Context context, String flowStage) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        switch (flowStage) {
            case PRE_FLOW:
                return sharedPreferences.getBoolean(context.getString(R.string.pref_preflow), context.getResources().getBoolean(R.bool.pre_flow_default));
            case SPLIT:
                return sharedPreferences.getBoolean(context.getString(R.string.pref_split), context.getResources().getBoolean(R.bool.split_default));
            case PRE_TRANSACTION:
                return sharedPreferences.getBoolean(context.getString(R.string.pref_prepayment), context.getResources().getBoolean(R.bool.pre_payment_default));
            case POST_CARD_READING:
                return sharedPreferences.getBoolean(context.getString(R.string.pref_postcard), context.getResources().getBoolean(R.bool.postcard_default));
            case POST_TRANSACTION:
                return sharedPreferences.getBoolean(context.getString(R.string.pref_postpayment), context.getResources().getBoolean(R.bool.post_payment_default));
            case POST_FLOW:
                return sharedPreferences.getBoolean(context.getString(R.string.pref_postflow), context.getResources().getBoolean(R.bool.post_flow_default));
            default:
                return false;
        }
    }

}
