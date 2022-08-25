/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.aevi.sdk.flow.service;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * ContentProvider base class that should be extended by API service providers in order to give information about the app capabilities.
 *
 * The implementing class will need to provide an implementation of the {@link #getServiceInfo()} method that should return
 * the serialised configuration. If this configuration is dynamic and changes then the implementing class should call
 * {@link #notifyServiceInfoChange()} on any changes so that the new configuration can be obtained by the system.
 */
public abstract class BaseServiceInfoProvider extends ContentProvider {

    public static final String SERVICE_INFO_KEY = "serviceInfo";
    public static final String SERVICE_INFO_ERROR_KEY = "serviceInfoError";
    public static final String SERVICE_INFO_ERROR_DESC_KEY = "serviceInfoErrorDescription";
    public static final String SERVICE_INFO_ERROR_HANDLED_KEY = "serviceInfoErrorHandled";
    public static final String METHOD_GET_SERVICE_INFO = "getServiceInfo";
    public static final String METHOD_PROVIDE_ERROR = "provideError";

    private final String serviceInfoChangeBroadcast;

    protected BaseServiceInfoProvider(String serviceInfoChangeBroadcast) {
        this.serviceInfoChangeBroadcast = serviceInfoChangeBroadcast;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    public String getType(Uri uri) {
        throw new UnsupportedOperationException();
    }

    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    public boolean onCreate() {
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        throw new UnsupportedOperationException();
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Bundle call(String method, String arg, Bundle extras) {
        Bundle bundle = new Bundle();
        if (method == null || method.isEmpty()) {
            method = METHOD_GET_SERVICE_INFO;
        }
        switch (method) {
            case METHOD_GET_SERVICE_INFO:
                bundle.putString(SERVICE_INFO_KEY, getServiceInfo());
                break;
            case METHOD_PROVIDE_ERROR:
                boolean handled = onServiceInfoError(extras.getString(SERVICE_INFO_ERROR_KEY), extras.getString(SERVICE_INFO_ERROR_DESC_KEY));
                bundle.putBoolean(SERVICE_INFO_ERROR_HANDLED_KEY, handled);
                break;
            default:
                break;
        }

        return bundle;
    }

    protected abstract String getServiceInfo();

    /**
     * Override this method to receive error messages related to the retrieval and/or parsing of the
     * {@link com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfo} provided by your application.
     *
     * @param errorType    The error type as per {@link com.aevi.sdk.flow.constants.ServiceInfoErrors}
     * @param errorMessage A descriptive error message
     * @return True if handled, false if not handled
     */
    protected boolean onServiceInfoError(@NonNull String errorType, @NonNull String errorMessage) {
        Log.e(getClass().getSimpleName(), "onServiceInfoError not implemented. type: " + errorType + ", message: " + errorMessage);
        return false;
    }

    /**
     * Notify the system that the configuration has changed.
     */
    public final void notifyServiceInfoChange() {
        notifyServiceInfoChange(getContext(), serviceInfoChangeBroadcast);
    }

    /**
     * Notify the system that the configuration has changed.
     *
     * @param context               context
     * @param configChangeBroadcast broadcast action
     */
    public static void notifyServiceInfoChange(Context context, String configChangeBroadcast) {
        String pkg = "package:" + context.getPackageName();
        Uri pkgUri = Uri.parse(pkg);
        context.sendBroadcast(new Intent(configChangeBroadcast).setData(pkgUri));
    }
}
