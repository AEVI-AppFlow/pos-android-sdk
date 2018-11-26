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
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.flow.service.BaseStatusUpdateService;
import com.aevi.sdk.flow.stage.StatusUpdateModel;
import com.aevi.sdk.pos.flow.model.Basket;
import com.aevi.sdk.pos.flow.model.BasketItem;

import static com.aevi.sdk.flow.constants.FlowTypes.FLOW_TYPE_BASKET_STATUS_UPDATE;
import static com.aevi.sdk.flow.constants.FlowTypes.FLOW_TYPE_CUSTOMER_STATUS_UPDATE;
import static com.aevi.sdk.flow.constants.StatusUpdateKeys.*;


public class StatusUpdateService extends BaseStatusUpdateService {

    private static final String TAG = StatusUpdateService.class.getSimpleName();

    @Override
    protected void processRequest(StatusUpdateModel stageModel) {
        String requestType = stageModel.getRequest().getRequestType();
        AdditionalData requestData = stageModel.getRequest().getRequestData();
        switch (requestType) {
            case FLOW_TYPE_BASKET_STATUS_UPDATE:
                handleBasketStatusUpdate(requestData);
                break;
            case FLOW_TYPE_CUSTOMER_STATUS_UPDATE:
                Customer customer = requestData.getValue(STATUS_UPDATE_CUSTOMER, Customer.class);
                Log.i(TAG, "Customer received: " + customer.toJson());
                break;
        }

        AdditionalData references = new AdditionalData();
        references.addData("exampleReference", "exampleValue");
        stageModel.finishWithReferences(references);
    }

    private void handleBasketStatusUpdate(AdditionalData requestData) {
        if (requestData.hasData(STATUS_UPDATE_BASKET_MODIFIED)) {
            Basket basket = requestData.getValue(STATUS_UPDATE_BASKET_MODIFIED, Basket.class);
            Log.i(TAG, "Basket received: " + basket.toJson());
        } else if (requestData.hasData(STATUS_UPDATE_BASKET_ITEMS_ADDED)) {
            BasketItem[] basketItems = requestData.getValue(STATUS_UPDATE_BASKET_ITEMS_ADDED, BasketItem[].class);
            Basket updatedItems = new Basket("addedItems", basketItems);
            Log.i(TAG, "Added basket items received: " + updatedItems.toJson());
        } else if (requestData.hasData(STATUS_UPDATE_BASKET_ITEMS_REMOVED)) {
            BasketItem[] basketItems = requestData.getValue(STATUS_UPDATE_BASKET_ITEMS_REMOVED, BasketItem[].class);
            Basket updatedItems = new Basket("removedItems", basketItems);
            Log.i(TAG, "Removed basket items received: " + updatedItems.toJson());
        } else if (requestData.hasData(STATUS_UPDATE_BASKET_ITEM_MODIFIED)) {
            BasketItem basketItem = requestData.getValue(STATUS_UPDATE_BASKET_ITEM_MODIFIED, BasketItem.class);
            Log.i(TAG, "Updated basket items received: " + basketItem.toString());
        }
    }
}
