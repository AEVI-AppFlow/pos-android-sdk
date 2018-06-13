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


import com.aevi.sdk.pos.flow.flowservicesample.ui.PostCardActivity;
import com.aevi.sdk.pos.flow.model.TransactionRequest;
import com.aevi.sdk.pos.flow.service.BasePostCardService;

public class PostCardService extends BasePostCardService {

    @Override
    protected void processRequest(String clientMessageId, TransactionRequest transactionRequest) {
        launchActivity(PostCardActivity.class, clientMessageId, transactionRequest);
    }

    @Override
    protected void finish(String clientMessageId) {
        finishLaunchedActivity(clientMessageId);
        finishWithNoResponse(clientMessageId);
    }
}
