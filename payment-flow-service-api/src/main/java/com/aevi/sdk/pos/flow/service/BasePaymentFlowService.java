package com.aevi.sdk.pos.flow.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.stage.GenericStageModel;
import com.aevi.sdk.flow.stage.PostGenericStageModel;
import com.aevi.sdk.pos.flow.PaymentFlowServiceApi;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.stage.*;

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
                    onPreFlow(PreFlowModel.fromService(this, clientMessageId, Payment.fromJson(request)));
                    break;
                case SPLIT:
                    onSplit(SplitModel.fromService(this, clientMessageId, SplitRequest.fromJson(request)));
                    break;
                case PRE_TRANSACTION:
                    onPreTransaction(PreTransactionModel.fromService(this, clientMessageId, TransactionRequest.fromJson(request)));
                    break;
                case PAYMENT_CARD_READING:
                    onPaymentCardReading(CardReadingModel.fromService(this, clientMessageId, TransactionRequest.fromJson(request)));
                    break;
                case POST_CARD_READING:
                    onPostCardReading(PreTransactionModel.fromService(this, clientMessageId, TransactionRequest.fromJson(request)));
                    break;
                case TRANSACTION_PROCESSING:
                    onTransactionProcessing(TransactionProcessingModel.fromService(this, clientMessageId, TransactionRequest.fromJson(request)));
                    break;
                case POST_TRANSACTION:
                    onPostTransaction(PostTransactionModel.fromService(this, clientMessageId, TransactionSummary.fromJson(request)));
                    break;
                case POST_FLOW:
                    onPostFlow(PostFlowModel.fromService(this, clientMessageId, PaymentResponse.fromJson(request)));
                    break;
                case GENERIC:
                    onGeneric(GenericStageModel.fromService(this, clientMessageId, Request.fromJson(request)));
                    break;
                case POST_GENERIC:
                    onPostGeneric(PostGenericStageModel.fromService(this, clientMessageId, Response.fromJson(request)));
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
    protected void onPreTransaction(PreTransactionModel model) {

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
     * Override to handle a request in the post-generic stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPostGeneric(PostGenericStageModel model) {

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
