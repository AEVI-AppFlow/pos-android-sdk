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

package com.aevi.sdk.pos.flow.paymentinitiationsample.model;


public class ApiFunction {

    public enum ApiMethod {
        SYSTEM_OVERVIEW,
        DEVICES,
        FLOW_SERVICES,
        SUBSCRIBE_EVENTS,
        GENERIC_REQUEST,
        RECEIPT_REQUEST,
        INITIATE_PAYMENT
    }

    private final ApiMethod apiMethod;
    private final String name;
    private final String description;

    public ApiFunction(ApiMethod apiMethod, String name, String description) {
        this.apiMethod = apiMethod;
        this.name = name;
        this.description = description;
    }

    public ApiMethod getApiMethod() {
        return apiMethod;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
