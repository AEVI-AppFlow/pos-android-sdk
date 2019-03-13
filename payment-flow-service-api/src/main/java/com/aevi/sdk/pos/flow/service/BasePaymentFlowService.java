package com.aevi.sdk.pos.flow.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.flow.stage.GenericStageModel;
import com.aevi.sdk.flow.stage.PostGenericStageModel;
import com.aevi.sdk.flow.stage.StatusUpdateModel;
import com.aevi.sdk.pos.flow.PaymentFlowServiceApi;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.stage.*;

import static com.aevi.sdk.flow.constants.ErrorConstants.STAGE_NOT_SUPPORTED;
import static com.aevi.sdk.flow.constants.FlowStages.*;
import static com.aevi.sdk.flow.constants.InternalDataKeys.FLOW_STAGE;

/**
 * Base class for payment flow services that wish to handle a request in a service directly.
 *
 * This class will map the flow stage to one of its callback methods that clients can override to handle the relevant stage.
 *
 * @see <a href="https://github.com/AEVI-AppFlow/pos-android-sdk/wiki/implementing-flow-services" target="_blank">Implementing Flow Services</a>
 */
public abstract class BasePaymentFlowService extends BaseApiService {

    public BasePaymentFlowService() {
        super(PaymentFlowServiceApi.getApiVersion());
    }

    @Override
    protected final void processRequest(@NonNull ClientCommunicator clientCommunicator, @NonNull String request,
                                        @Nullable InternalData senderInternalData) {
        mapStageToCallback(clientCommunicator, request, senderInternalData);
    }

    /**
     * Maps a stage to one of the callback methods that can be overridden in this class.
     *
     * @param senderInternalData The internal data of the client that initiated this flow
     * @param clientCommunicator The client message communicator
     * @param request            The request
     */
    private void mapStageToCallback(ClientCommunicator clientCommunicator, String request, InternalData senderInternalData) {
        try {
            String flowStage = getInternalData(senderInternalData, FLOW_STAGE);
            if (flowStage != null) {
                Log.d(BasePaymentFlowService.class.getSimpleName(),
                      String.format("Mapping request for flow stage: %s", flowStage));
                switch (flowStage) {
                    case PRE_FLOW:
                        onPreFlow(PreFlowModel.fromService(clientCommunicator, Payment.fromJson(request), senderInternalData));
                        break;
                    case SPLIT:
                        onSplit(SplitModel.fromService(clientCommunicator, SplitRequest.fromJson(request), senderInternalData));
                        break;
                    case PRE_TRANSACTION:
                        onPreTransaction(
                                PreTransactionModel.fromService(clientCommunicator, TransactionRequest.fromJson(request), senderInternalData));
                        break;
                    case PAYMENT_CARD_READING:
                        onPaymentCardReading(
                                CardReadingModel.fromService(clientCommunicator, TransactionRequest.fromJson(request), senderInternalData));
                        break;
                    case POST_CARD_READING:
                        onPostCardReading(
                                PreTransactionModel.fromService(clientCommunicator, TransactionRequest.fromJson(request), senderInternalData));
                        break;
                    case TRANSACTION_PROCESSING:
                        onTransactionProcessing(
                                TransactionProcessingModel.fromService(clientCommunicator, TransactionRequest.fromJson(request), senderInternalData));
                        break;
                    case POST_TRANSACTION:
                        onPostTransaction(
                                PostTransactionModel.fromService(clientCommunicator, TransactionSummary.fromJson(request), senderInternalData));
                        break;
                    case POST_FLOW:
                        onPostFlow(PostFlowModel.fromService(clientCommunicator, PaymentResponse.fromJson(request), senderInternalData));
                        break;
                    case GENERIC:
                        onGeneric(GenericStageModel.fromService(clientCommunicator, Request.fromJson(request), senderInternalData));
                        break;
                    case POST_GENERIC:
                        onPostGeneric(PostGenericStageModel.fromService(clientCommunicator, Response.fromJson(request), senderInternalData));
                        break;
                    case STATUS_UPDATE:
                        onStatusUpdate(StatusUpdateModel.fromService(clientCommunicator, Request.fromJson(request), senderInternalData));
                        break;
                    default:
                        onUnknownStage(flowStage, clientCommunicator, request);
                        break;

                }
            }
        } catch (StageNotImplementedException e) {
            returnStageNotImplemented(clientCommunicator, e.stage);
        }
    }

    /**
     * Override to handle a request in the pre-flow stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPreFlow(PreFlowModel model) {
        throw new StageNotImplementedException(PRE_FLOW);
    }

    /**
     * Override to handle a request in the split stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onSplit(SplitModel model) {
        throw new StageNotImplementedException(SPLIT);
    }

    /**
     * Override to handle a request in the pre-transaction stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPreTransaction(PreTransactionModel model) {
        throw new StageNotImplementedException(PRE_TRANSACTION);
    }

    /**
     * Override to handle a request in the payment-card-reading stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPaymentCardReading(CardReadingModel model) {
        throw new StageNotImplementedException(PAYMENT_CARD_READING);
    }

    /**
     * Override to handle a request in the post-card-reading stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPostCardReading(PreTransactionModel model) {
        throw new StageNotImplementedException(POST_CARD_READING);
    }

    /**
     * Override to handle a request in the transaction-processing stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onTransactionProcessing(TransactionProcessingModel model) {
        throw new StageNotImplementedException(TRANSACTION_PROCESSING);
    }

    /**
     * Override to handle a request in the post-transaction stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPostTransaction(PostTransactionModel model) {
        throw new StageNotImplementedException(POST_TRANSACTION);
    }

    /**
     * Override to handle a request in the post-flow stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPostFlow(PostFlowModel model) {
        throw new StageNotImplementedException(POST_FLOW);
    }

    /**
     * Override to handle a request in the generic stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onGeneric(GenericStageModel model) {
        throw new StageNotImplementedException(GENERIC);
    }

    /**
     * Override to handle a request in the post-generic stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onPostGeneric(PostGenericStageModel model) {
        throw new StageNotImplementedException(POST_GENERIC);
    }

    /**
     * Override to handle a request in the status-update stage.
     *
     * @param model The model relevant for this stage
     */
    protected void onStatusUpdate(StatusUpdateModel model) {
        throw new StageNotImplementedException(POST_GENERIC);
    }

    /**
     * Fallback method when the flow stage can not be mapped to any of the known callback methods.
     *
     * The default implementation here will throw an exception. Clients can override to implement an alternative fallback behaviour.
     *
     * @param flowStage          The flow stage that could not be mapped
     * @param clientCommunicator The client message id
     * @param request            The request
     */
    protected void onUnknownStage(String flowStage, ClientCommunicator clientCommunicator, String request) {
        throw new StageNotImplementedException(flowStage);
    }

    private void returnStageNotImplemented(ClientCommunicator clientCommunicator, String stage) {
        clientCommunicator
                .sendResponseAsErrorAndEnd(STAGE_NOT_SUPPORTED, String.format("[%s] Stage handling not implemented by this flow service.", stage));
    }

    private static class StageNotImplementedException extends RuntimeException {

        final String stage;

        StageNotImplementedException(String stage) {
            this.stage = stage;
        }
    }
}
