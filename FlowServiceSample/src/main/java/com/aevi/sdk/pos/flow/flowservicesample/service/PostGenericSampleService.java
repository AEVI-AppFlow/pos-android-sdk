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

import android.util.Log;
import android.widget.Toast;

import com.aevi.sdk.flow.service.BasePostGenericService;
import com.aevi.sdk.flow.stage.PostGenericStageModel;

public class PostGenericSampleService extends BasePostGenericService {

    @Override
    protected void processResponse(PostGenericStageModel stageModel) {
        Toast.makeText(this, "Received post-generic response data!", Toast.LENGTH_SHORT).show();
        Log.d(PostGenericSampleService.class.getSimpleName(), "processResponse: " + stageModel.getResponse().toJson());
        stageModel.addReferences("postGeneric", "wasHere");
        stageModel.sendResponse();
    }

    @Override
    protected void onFinish() {
        // No-op
    }
}
