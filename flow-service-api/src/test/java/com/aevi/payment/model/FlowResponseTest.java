package com.aevi.payment.model;


import com.aevi.sdk.pos.flow.model.FlowResponse;

import org.junit.Test;

public class FlowResponseTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowAmountsPaidLargerThanUpdatedAmounts() throws Exception {
        com.aevi.sdk.pos.flow.model.FlowResponse flowResponse = new com.aevi.sdk.pos.flow.model.FlowResponse();

        flowResponse.updateRequestAmounts(new com.aevi.sdk.pos.flow.model.FlowAmountsBuilder().updateBaseAmount(1000).buildAmounts());
        flowResponse.setAmountsPaid(new com.aevi.sdk.pos.flow.model.Amounts(2000, "GBP"), "cash");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowDifferentAmountCurrencies() throws Exception {
        com.aevi.sdk.pos.flow.model.FlowResponse flowResponse = new FlowResponse();

        flowResponse.setAmountsPaid(new com.aevi.sdk.pos.flow.model.Amounts(1000, "GBP"), "cash");
        flowResponse.updateRequestAmounts(new com.aevi.sdk.pos.flow.model.FlowAmountsBuilder().updateBaseAmount(1000).changeCurrency("EUR").buildAmounts());
    }
}
