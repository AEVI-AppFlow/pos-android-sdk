package com.aevi.sdk.pos.flow.stage;

import com.aevi.sdk.flow.model.AppMessage;
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

public class SplitModelTest {

    private ClientCommunicator clientCommunicator;
    private PublishSubject<AppMessage> messageSubject = PublishSubject.create();
    private SplitModel splitModel;
    private SplitRequest splitRequest;
    private long totalRequestValue;
    private Basket paymentBasket;
    private List<Transaction> prevTransactions;
    private InternalData internalData;

    @Before
    public void setUp() throws Exception {
        clientCommunicator = mock(ClientCommunicator.class);
        internalData = mock(InternalData.class);
        when(clientCommunicator.subscribeToMessages()).thenReturn(messageSubject);
        totalRequestValue = 1000;
        paymentBasket = new Basket("basket", new BasketItemBuilder().withLabel("item")
                .withAmount(totalRequestValue / 2).withQuantity(2).build());
        Payment payment = new PaymentBuilder().withAmounts(new Amounts(totalRequestValue, "GBP"))
                .withBasket(paymentBasket).withPaymentFlow("sale").build();
        prevTransactions = new ArrayList<>();
        splitRequest = new SplitRequest(payment, payment.getAmounts(), prevTransactions);
        splitModel = SplitModel.fromService(clientCommunicator, splitRequest, internalData);
    }

    @Test
    public void setBaseAmountForNextTransactionShouldBeSetCorrectly() throws Exception {
        splitModel.setBaseAmountForNextTransaction(200);

        splitModel.sendResponse();

        FlowResponse flowResponse = getSentFlowResponse();
        assertThat(flowResponse.getUpdatedRequestAmounts().getBaseAmountValue()).isEqualTo(200);
        assertThat(flowResponse.getUpdatedRequestAmounts().getCurrency()).isEqualTo("GBP");
    }

    @Test
    public void setBasketForNextTransactionShouldUpdateBaseAmountCorrectly() throws Exception {
        Basket basket = new Basket("txnOneBasket", new BasketItemBuilder().withLabel("item")
                .withAmount(totalRequestValue / 2).withQuantity(1).build());
        splitModel.setBasketForNextTransaction(basket);

        splitModel.sendResponse();

        FlowResponse flowResponse = getSentFlowResponse();
        assertThat(flowResponse.getAdditionalBasket()).isEqualTo(basket);
        assertThat(flowResponse.getUpdatedRequestAmounts().getBaseAmountValue()).isEqualTo(basket.getTotalBasketValue());
        assertThat(flowResponse.getUpdatedRequestAmounts().getCurrency()).isEqualTo("GBP");
    }

    @Test
    public void shouldReportLastTransactionFailedAsFalseIfNoTransactions() throws Exception {
        assertThat(splitModel.lastTransactionFailed()).isFalse();
    }

    @Test
    public void shouldReportLastTransactionFailedAsTrueIfPrevTxnFailed() throws Exception {
        Basket prevTxnBasket = new Basket("txnOneBasket", new BasketItemBuilder().withLabel("item")
                .withAmount(totalRequestValue / 2).withQuantity(1).build());
        Transaction prevTxn = new Transaction(new Amounts(totalRequestValue / 2, "GBP"), Arrays.asList(prevTxnBasket),
                                              null, null);
        prevTxn.addTransactionResponse(new TransactionResponseBuilder(prevTxn.getId()).decline("failed").build());
        prevTransactions.add(prevTxn);

        assertThat(splitModel.lastTransactionFailed()).isTrue();
    }

    @Test
    public void cancelFlowShouldSetCancelFlagAndSend() throws Exception {
        splitModel.cancelFlow();

        FlowResponse flowResponse = getSentFlowResponse();
        assertThat(flowResponse.shouldCancelTransaction()).isTrue();
    }

    @Test
    public void skipShouldSendEmptyResponse() throws Exception {
        splitModel.skip();

        FlowResponse flowResponse = getSentFlowResponse();
        assertThat(flowResponse).isEqualTo(new FlowResponse());
    }

    private FlowResponse getSentFlowResponse() {
        ArgumentCaptor<AppMessage> captor = ArgumentCaptor.forClass(AppMessage.class);
        verify(clientCommunicator).sendMessage(captor.capture());
        return FlowResponse.fromJson(captor.getValue().getMessageData());
    }
}
