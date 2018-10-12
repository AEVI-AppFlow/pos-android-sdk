package com.aevi.sdk.pos.flow.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.stage.GenericStageModel;
import com.aevi.sdk.pos.flow.PaymentFlowServiceApi;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.stage.CardReadingModel;
import com.aevi.sdk.pos.flow.stage.PostFlowModel;
import com.aevi.sdk.pos.flow.stage.PostTransactionModel;
import com.aevi.sdk.pos.flow.stage.PreFlowModel;
import com.aevi.sdk.pos.flow.stage.PreTransactionModel;
import com.aevi.sdk.pos.flow.stage.SplitModel;
import com.aevi.sdk.pos.flow.stage.TransactionProcessingModel;

import static com.aevi.sdk.flow.constants.FlowStages.*;

/**
 * Base class for payment flow services that wish to handle a request in a service directly.
 *
 * This class will map the flow stage to one of its callback methods that clients can override to handle the relevant stage.
 */
public abstract class BasePaymentFlowService extends BaseApiService {

    public BasePaymentFlowService() {
        super(PaymentFlowServiceApi.getApiVersion());
    }

    @Override
    protected void processRequest(@NonNull String clientMessageId, @NonNull String request, @NonNull String flowStage) {
        Log.d(BasePaymentFlowService.class.getSimpleName(), "Mapping request for flow stage: " + flowStage);
        mapStageToCallback(flowStage, clientMessageId, request);
    }

    /**
     * Maps a stage to one of the callback methods that can be overridden in this class.
     *
     * @param flowStage       The flow stage
     * @param clientMessageId The client message id
     * @param request         The request
     */
    protected void mapStageToCallback(String flowStage, String clientMessageId, String request) {
        if (flowStage != null) {
            switch (flowStage) {
                case PRE_FLOW:
                    onPreFlow(com.aevi.sdk.pos.flow.stage.PreFlowModel.fromService(this, clientMessageId, Payment.fromJson(request)));
                    break;
                case SPLIT:
                    onSplit(com.aevi.sdk.pos.flow.stage.SplitModel.fromService(this, clientMessageId, SplitRequest.fromJson(request)));
                    break;
                case PRE_TRANSACTION:
                    onPreTransaction(com.aevi.sdk.pos.flow.stage.PreTransactionModel.fromService(this, clientMessageId, TransactionRequest.fromJson(request)));
                    break;
                case PAYMENT_CARD_READING:
                    onPaymentCardReading(com.aevi.sdk.pos.flow.stage.CardReadingModel.fromService(this, clientMessageId, TransactionRequest.fromJson(request)));
                    break;
                case POST_CARD_READING:
                    onPostCardReading(com.aevi.sdk.pos.flow.stage.PreTransactionModel.fromService(this, clientMessageId, TransactionRequest.fromJson(request)));
                    break;
                case TRANSACTION_PROCESSING:
                    onTransactionProcessing(com.aevi.sdk.pos.flow.stage.TransactionProcessingModel.fromService(this, clientMessageId, TransactionRequest.fromJson(request)));
                    break;
                case POST_TRANSACTION:
                    onPostTransaction(com.aevi.sdk.pos.flow.stage.PostTransactionModel.fromService(this, clientMessageId, TransactionSummary.fromJson(request)));
                    break;
                case POST_FLOW:
                    onPostFlow(com.aevi.sdk.pos.flow.stage.PostFlowModel.fromService(this, clientMessageId, PaymentResponse.fromJson(request)));
                    break;
                case GENERIC:
                    onGeneric(GenericStageModel.fromService(this, clientMessageId, Request.fromJson(request)));
                    break;
                default:
                    onUnknownStage(flowStage, clientMessageId, request);
                    break;

            }
        }
    }

    /**
     * Override to handle a request in the pre-flow stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPreFlow(PreFlowModel model) {

    }

    /**
     * Override to handle a request in the split stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onSplit(SplitModel model) {

    }

    /**
     * Override to handle a request in the pre-transaction stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPreTransaction(com.aevi.sdk.pos.flow.stage.PreTransactionModel model) {

    }

    /**
     * Override to handle a request in the payment-card-reading stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPaymentCardReading(CardReadingModel model) {

    }

    /**
     * Override to handle a request in the post-card-reading stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPostCardReading(PreTransactionModel model) {

    }

    /**
     * Override to handle a request in the transaction-processing stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onTransactionProcessing(TransactionProcessingModel model) {

    }

    /**
     * Override to handle a request in the post-transaction stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPostTransaction(PostTransactionModel model) {

    }

    /**
     * Override to handle a request in the post-flow stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPostFlow(PostFlowModel model) {

    }

    /**
     * Override to handle a request in the generic stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onGeneric(GenericStageModel model) {

    }

    /**
     * Fallback method when the flow stage can not be mapped to any of the known callback methods.
     *
     * The default implementation here will throw an exception. Clients can override to implement an alternative fallback behaviour.
     *
     * @param flowStage       The flow stage that could not be mapped
     * @param clientMessageId The client message id
     * @param request         The request
     */
    protected void onUnknownStage(String flowStage, String clientMessageId, String request) {
        throw new IllegalArgumentException("Unknown stage: " + flowStage);
    }


}
