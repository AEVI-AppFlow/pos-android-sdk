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


import com.aevi.sdk.flow.FlowApi;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;

/**
 * Base service for handling generic requests defined by a request type and associated bespoke data.
 *
 * See documentation for examples and reference types and data.
 */
public abstract class BaseRequestService extends BaseApiService<Request, Response> {

    public BaseRequestService() {
        super(Request.class, FlowApi.getApiVersion());
    }
}
