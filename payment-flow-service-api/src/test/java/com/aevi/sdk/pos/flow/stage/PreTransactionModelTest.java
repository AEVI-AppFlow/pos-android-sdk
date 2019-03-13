package com.aevi.sdk.pos.flow.stage;

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.pos.flow.model.*;
import io.reactivex.subjects.PublishSubject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PreTransactionModelTest {

    private PreTransactionModel preTransactionModel;
    private ClientCommunicator clientCommunicator;
    private PublishSubject<AppMessage> messageSubject = PublishSubject.create();
    private TransactionRequest transactionRequest;
    private List<Basket> requestBaskets;
    private Customer customer;
    private AdditionalData additionalData;
    private Card card;
    private long baseAmountValue;
    private InternalData internalData;

    @Before
    public void setUp() throws Exception {
        clientCommunicator = mock(ClientCommunicator.class);
        internalData = mock(InternalData.class);
        when(clientCommunicator.subscribeToMessages()).thenReturn(messageSubject);
        customer = new Customer("custId");
        requestBaskets = new ArrayList<>();
        additionalData = new AdditionalData();
        card = Card.getEmptyCard();
        baseAmountValue = 100;
        requestBaskets.add(new Basket("basket", new BasketItemBuilder().withLabel("item").withAmount(baseAmountValue).build()));
        transactionRequest = new TransactionRequest("myId", "txnId", "sale", "PRE_TRANSACTION",
                                                    new Amounts(baseAmountValue, "GBP"), requestBaskets, customer, additionalData, card);
        preTransactionModel = PreTransactionModel.fromService(clientCommunicator, transactionRequest, internalData);
    }

    @Test
    public void shouldAllowAddingAdditionalAmounts() throws Exception {
        preTransactionModel.setAdditionalAmount("testAmount", 500);

        preTransactionModel.sendResponse();

        FlowResponse response = getSentFlowResponse();
        assertThat(response.getUpdatedRequestAmounts().getTotalAmountValue()).isEqualTo(baseAmountValue + 500);
        assertThat(response.getUpdatedRequestAmounts().getAdditionalAmountValue("testAmount")).isEqualTo(500);
    }

    @Test
    public void shouldAllowAddingAdditionalAmountsAsFraction() throws Exception {
        preTransactionModel.setAdditionalAmountAsBaseFraction("fractionAmount", 0.5f);

        preTransactionModel.sendResponse();

        FlowResponse response = getSentFlowResponse();
        assertThat(response.getUpdatedRequestAmounts().getTotalAmountValue()).isEqualTo((long) (baseAmountValue * 1.5));
        assertThat(response.getUpdatedRequestAmounts().getAdditionalAmountValue("fractionAmount")).isEqualTo((long) (baseAmountValue / 2));
    }

    @Test
    public void shouldAddNewBasketAndUpdateRequestAmountsAccordingly() throws Exception {
        Basket newBasket = new Basket("newBasket", new BasketItemBuilder().withLabel("item").withAmount(200).build());
        preTransactionModel.addNewBasket(newBasket);

        preTransactionModel.sendResponse();

        FlowResponse response = getSentFlowResponse();
        assertThat(response.getAdditionalBasket()).isEqualTo(newBasket);
        assertThat(response.getUpdatedRequestAmounts().getBaseAmountValue()).isEqualTo(baseAmountValue + 200);
    }

    @Test
    public void shouldSetAmountsPaidForAppliedDiscountItems() throws Exception {
        List<BasketItem> discountItems = new ArrayList<>();
        discountItems.add(new BasketItemBuilder().withLabel("newItem").withAmount(baseAmountValue * -1).build());

        preTransactionModel.applyDiscountsToBasket(requestBaskets.get(0).getId(), discountItems, "reward");
        preTransactionModel.sendResponse();

        FlowResponse response = getSentFlowResponse();
        assertThat(response.getModifiedBasket().getId()).isEqualTo(requestBaskets.get(0).getId());
        assertThat(response.getModifiedBasket().getBasketItems()).isEqualTo(discountItems);
        assertThat(response.getAmountsPaid()).isEqualTo(new Amounts(baseAmountValue, "GBP"));
        assertThat(response.getAmountsPaidPaymentMethod()).isEqualTo("reward");
    }

    @Test
    public void skipShouldSendEmptyResponse() throws Exception {
        preTransactionModel.skip();

        FlowResponse flowResponse = getSentFlowResponse();
        assertThat(flowResponse).isEqualTo(new FlowResponse());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentForApplyDiscountsWhenThereAreNoBaskets() throws Exception {
        transactionRequest.getBaskets().clear();
        preTransactionModel.applyDiscountsToBasket("123", new ArrayList<BasketItem>(), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentForPositiveBasketItemsWhenApplyingDiscounts() throws Exception {
        requestBaskets.add(new Basket("basket", new BasketItemBuilder().withLabel("item").withAmount(200).build()));
        preTransactionModel.applyDiscountsToBasket("123", Arrays.asList(new BasketItemBuilder().withLabel("newItem").withAmount(200).build()), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNegativeNewBasketValue() throws Exception {
        Basket newBasket = new Basket("newBasket", new BasketItemBuilder().withLabel("item").withAmount(-200).build());
        preTransactionModel.addNewBasket(newBasket);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentForDifferentPaidCurrency() throws Exception {
        preTransactionModel.setAmountsPaid(new Amounts(100, "SEK"), "reward");
    }

    private FlowResponse getSentFlowResponse() {
        ArgumentCaptor<AppMessage> captor = ArgumentCaptor.forClass(AppMessage.class);
        verify(clientCommunicator).sendMessage(captor.capture());
        return FlowResponse.fromJson(captor.getValue().getMessageData());
    }
}
