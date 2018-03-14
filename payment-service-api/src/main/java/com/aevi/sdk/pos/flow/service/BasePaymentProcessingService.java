package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.PaymentServiceApi;
import com.aevi.sdk.pos.flow.model.TransactionRequest;
import com.aevi.sdk.pos.flow.model.TransactionResponse;

/**
 * This abstract service should be extended to provide a payment service implementation
 *
 * The {@code initiatePayment(String, REQUEST)} method should be implemented in the parent class and should perform the job of completing the
 * transaction or responding with an error.
 *
 * The implementing class should be added as a service in the <code>AndroidManifest.xml</code> of your application and should also provide some meta-data information.
 *
 * <p><strong>payment-service-label</strong> - Is a string that is used when the name of the payment application should be displayed for selection by a user</p>
 * <p><strong>configuration-authority</strong> - Is the authority name of a corresponding content provider that will provide payment service configuration info</p>
 *
 * <pre>
 *     {@code
 *          <service android:name=".services.PaymentService" android:exported="true">
 *              <intent-filter>
 *                  <action android:name="com.aevi.payment.PROCESS_PAYMENT"/>
 *              </intent-filter>
 *
 *              <meta-data
 *                      android:name="configuration-authority"
 *                      android:value="com.example.payment.service.config"/>
 *              <meta-data
 *                      android:name="payment-service-label"
 *                      android:value="Example Payment Service"/>
 *          </service>
 *
 *          <provider
 *              android:name=".services.ConfigurationProvider"
 *              android:authorities="com.example.payment.service.config"
 *              android:exported="true"/>
 *      }
 * </pre>
 *
 * The configuration provider should extend {@link BasePaymentServiceInfoProvider}
 */
public abstract class BasePaymentProcessingService extends BaseApiService<TransactionRequest, TransactionResponse> {

    public BasePaymentProcessingService() {
        super(TransactionRequest.class, PaymentServiceApi.getApiVersion());
    }
}
