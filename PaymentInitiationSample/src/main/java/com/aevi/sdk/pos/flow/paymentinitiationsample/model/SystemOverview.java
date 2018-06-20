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


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class SystemOverview {

    private int numFlowServices;
    private int numPaymentServices;
    private int numDevices;
    private Collection<String> allPaymentMethods = new HashSet<>();
    private Collection<String> allCurrencies = new ArrayList<>();
    private Collection<String> allRequestTypes = new HashSet<>();
    private Collection<String> allTransactionTypes = new ArrayList<>();
    private Collection<String> allDataKeys = new HashSet<>();

    public int getNumFlowServices() {
        return numFlowServices;
    }

    public void setNumFlowServices(int numFlowServices) {
        this.numFlowServices = numFlowServices;
    }

    public int getNumPaymentServices() {
        return numPaymentServices;
    }

    public void setNumPaymentServices(int numPaymentServices) {
        this.numPaymentServices = numPaymentServices;
    }

    public int getNumDevices() {
        return numDevices;
    }

    public void setNumDevices(int numDevices) {
        this.numDevices = numDevices;
    }

    public String[] getAllPaymentMethods() {
        return allPaymentMethods.toArray(new String[allPaymentMethods.size()]);
    }

    public void addPaymentMethods(Collection<String> allPaymentMethods) {
        this.allPaymentMethods.addAll(allPaymentMethods);
    }

    public String[] getAllCurrencies() {
        return allCurrencies.toArray(new String[allCurrencies.size()]);
    }

    public void setAllCurrencies(Collection<String> allCurrencies) {
        this.allCurrencies = allCurrencies;
    }

    public String[] getAllRequestTypes() {
        return allRequestTypes.toArray(new String[allRequestTypes.size()]);
    }

    public void addRequestTypes(Collection<String> allRequestTypes) {
        this.allRequestTypes.addAll(allRequestTypes);
    }

    public String[] getAllTransactionTypes() {
        return allTransactionTypes.toArray(new String[allTransactionTypes.size()]);
    }

    public void setAllTransactionTypes(Collection<String> allTransactionTypes) {
        this.allTransactionTypes = allTransactionTypes;
    }

    public String[] getAllDataKeys() {
        return allDataKeys.toArray(new String[allDataKeys.size()]);
    }

    public void addDataKeys(Collection<String> allDataKeys) {
        this.allDataKeys.addAll(allDataKeys);
    }
}
