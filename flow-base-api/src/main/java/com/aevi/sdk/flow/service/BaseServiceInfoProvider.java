package com.aevi.sdk.flow.service;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

/**
 * ContentProvider base class that should be extended by API service providers in order to give information about the app capabilities.
 *
 * The implementing class will need to provide an implementation of the {@link #getServiceInfo()} method that should return
 * the serialised configuration. If this configuration is dynamic and changes then the implementing class should call
 * {@link #notifyConfigurationChange()} on any changes so that the new configuration can be obtained by the system.
 */
public abstract class BaseServiceInfoProvider extends ContentProvider {

    public static final String CONFIGURATION_KEY = "configuration";

    private final String configChangeBroadcast;

    protected BaseServiceInfoProvider(String configChangeBroadcast) {
        this.configChangeBroadcast = configChangeBroadcast;
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
        Bundle b = new Bundle();
        b.putString(CONFIGURATION_KEY, getServiceInfo());
        return b;
    }

    protected abstract String getServiceInfo();

    /**
     * Notify the system that the configuration has changed.
     */
    public final void notifyConfigurationChange() {
        notifyConfigurationChange(getContext(), configChangeBroadcast);
    }

    /**
     * Notify the system that the configuration has changed.
     */
    public static void notifyConfigurationChange(Context context, String configChangeBroadcast) {
        String pkg = "package:" + context.getPackageName();
        Uri pkgUri = Uri.parse(pkg);
        context.sendBroadcast(new Intent(configChangeBroadcast).setData(pkgUri));
    }
}
