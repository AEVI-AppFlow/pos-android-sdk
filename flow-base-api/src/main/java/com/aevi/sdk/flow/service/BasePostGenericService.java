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
import com.aevi.sdk.flow.FlowBaseConfig;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.stage.PostGenericStageModel;

/**
 * Base service for handling post-generic stages which receive the response from the main generic stage.
 *
 * See documentation for examples and reference types and data.
 */
public abstract class BasePostGenericService extends BaseApiService {

    protected BasePostGenericService() {
        super(FlowBaseConfig.VERSION);
    }

    @Override
    protected void processRequest(@NonNull ClientCommunicator clientCommunicator, @NonNull String request, @NonNull String flowStage) {
        PostGenericStageModel postGenericStageModel = PostGenericStageModel.fromService(clientCommunicator, Response.fromJson(request));
        processResponse(postGenericStageModel);
    }

    /**
     * Handle the generic response.
     *
     * @param stageModel The post-generic stage model
     */
    protected abstract void processResponse(PostGenericStageModel stageModel);
}
