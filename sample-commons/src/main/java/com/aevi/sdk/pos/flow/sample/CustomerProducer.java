package com.aevi.sdk.pos.flow.sample;

import com.aevi.sdk.flow.constants.CustomerDataKeys;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.flow.model.Token;

import java.util.UUID;

public class CustomerProducer {

    private static final String CUSTOMER_ID = UUID.randomUUID().toString();
    private static final String CUSTOMER_NAME = "Joanna Doe";
    private static final String CUSTOMER_EMAIL = "joanna@doe.com";
    private static final String CUSTOMER_PHONE = "12345678";

    public static final Token CUSTOMER_TOKEN = new Token(UUID.randomUUID().toString(), "card", "random");

    public static Customer getDefaultCustomer(String source) {
        Customer customer = new Customer(CUSTOMER_ID);
        customer.setFullName(CUSTOMER_NAME);
        customer.addCustomerDetails(CustomerDataKeys.EMAIL, CUSTOMER_EMAIL);
        customer.addCustomerDetails(CustomerDataKeys.PHONE, CUSTOMER_PHONE);
        customer.addCustomerDetails("generatedBy", source);
        customer.addToken(CUSTOMER_TOKEN);
        return customer;
    }
}
