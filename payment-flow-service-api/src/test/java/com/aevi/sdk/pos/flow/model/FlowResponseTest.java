package com.aevi.sdk.pos.flow.model;


import org.junit.Test;

public class FlowResponseTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowAmountsPaidLargerThanUpdatedAmounts() throws Exception {
        FlowResponse flowResponse = new FlowResponse();

        flowResponse.updateRequestAmounts(new Amounts(1000, "GBP"));
        flowResponse.setAmountsPaid(new Amounts(2000, "GBP"), "cash");
        flowResponse.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowDifferentAmountCurrencies() throws Exception {
        FlowResponse flowResponse = new FlowResponse();

        flowResponse.setAmountsPaid(new Amounts(1000, "GBP"), "cash");
        flowResponse.updateRequestAmounts(new Amounts(1000, "EUR"));
        flowResponse.validate();
    }
}
