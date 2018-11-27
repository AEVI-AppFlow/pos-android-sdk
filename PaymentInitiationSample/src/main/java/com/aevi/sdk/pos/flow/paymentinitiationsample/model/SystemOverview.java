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


import com.aevi.sdk.flow.model.config.FpsSettings;
import com.aevi.sdk.pos.flow.model.PaymentFlowServices;
import com.aevi.sdk.pos.flow.model.config.FlowConfigurations;

public class SystemOverview {

    private int numDevices;
    private PaymentFlowServices paymentFlowServices;
    private FlowConfigurations flowConfigurations;
    private FpsSettings fpsSettings;

    public int getNumDevices() {
        return numDevices;
    }

    public void setNumDevices(int numDevices) {
        this.numDevices = numDevices;
    }

    public PaymentFlowServices getPaymentFlowServices() {
        return paymentFlowServices;
    }

    public void setPaymentFlowServices(PaymentFlowServices paymentFlowServices) {
        this.paymentFlowServices = paymentFlowServices;
    }

    public void setFlowConfigurations(FlowConfigurations flowConfigurations) {
        this.flowConfigurations = flowConfigurations;
    }

    public FlowConfigurations getFlowConfigurations() {
        return flowConfigurations;
    }

    public void setFpsSettings(FpsSettings fpsSettings) {
        this.fpsSettings = fpsSettings;
    }

    public FpsSettings getFpsSettings() {
        return fpsSettings;
    }
}
