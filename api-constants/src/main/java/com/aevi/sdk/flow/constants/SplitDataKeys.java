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


public interface SplitDataKeys {

    String DATA_KEY_SPLIT_TXN = "splitTxn"; // boolean. true if part of a split, false or not set otherwise
    String DATA_KEY_NUM_SPLITS = "numSplits"; // int. The number of splits in total
    String DATA_KEY_SPLIT_TYPE = "splitType"; // String. The type of split

    String SPLIT_TYPE_BASKET = "splitBasketItems";
    String SPLIT_TYPE_AMOUNTS = "splitAmounts";

}
