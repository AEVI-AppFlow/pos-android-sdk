package com.aevi.sdk.pos.flow.service;

import com.aevi.sdk.flow.model.NoOpModel;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.util.ApiVersionProvider;
import com.aevi.sdk.pos.flow.model.TokenResponse;

/**
 * This abstract service should be extended to provide a tokenisation service implementation
 *
 * The implementing class should be added as a service in the <code>AndroidManifest.xml</code> of your application.
 *
 * <pre>
 *     {@code
 *      <service
 *          android:name=".services.TokenizeService"
 *          android:exported="true">
 *          <intent-filter>
 *              <action android:name="com.aevi.payment.HANDLE_TOKENIZE"/>
 *          </intent-filter>
 *      </service>
 *      }
 * </pre>
 *
 * The configuration provider should extend {@link BasePaymentServiceInfoProvider}
 */
public abstract class BaseTokenizeService extends BaseApiService<NoOpModel, TokenResponse> {

    public BaseTokenizeService() {
        super(NoOpModel.class, ApiVersionProvider.getApiVersion(ApiProperties.API_PROPERTIES_FILE));
    }

    @Override
    protected final void processRequest(String clientMessageId, NoOpModel noOpModel) {
        processRequest(clientMessageId);
    }

    protected abstract void processRequest(String clientMessageId);
}
