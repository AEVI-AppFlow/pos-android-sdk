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

package com.aevi.sdk.flow.model.config;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import static com.aevi.android.rxmessenger.MessageConstants.CHANNEL_MESSENGER;

/**
 * General AppFlow settings that may be useful to any application integrated with AppFlow.
 *
 * Unlike other settings/configurations that are based on values from flow services on the device, these values are defined by the acquirer and/or
 * merchant to be relevant for the particular environment the device is operating in.
 */
public class AppFlowSettings implements Jsonable {

    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd"; // 2018-01-01
    public static final String TIME_FORMAT_DEFAULT = "HH:mm:ss"; // 13:00:00
    public static final String PRIMARY_CURRENCY_DEFAULT = null; // No default currency (has to be set)
    public static final String PRIMARY_LANGUAGE_DEFAULT = "en"; // English
    public static final String COMMS_CHANNEL_DEFAULT = CHANNEL_MESSENGER;

    private String dateFormat = DATE_FORMAT_DEFAULT;
    private String timeFormat = TIME_FORMAT_DEFAULT;
    private String primaryCurrency = PRIMARY_CURRENCY_DEFAULT;
    private String primaryLanguage = PRIMARY_LANGUAGE_DEFAULT;
    private String commsChannel = COMMS_CHANNEL_DEFAULT;

    /**
     * Get the date format to use for formatting dates, as defined by the acquirer/merchant.
     *
     * This can be used with the Java SimpleDateFormat class to format dates.
     *
     * See {@link #DATE_FORMAT_DEFAULT} for default.
     *
     * @return The date format to use for formatting dates as defined by the acquirer/merchant
     */
    @NonNull
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Set the date format to use for formatting dates, as defined by the acquirer/merchant.
     *
     * This can be used with the Java SimpleDateFormat class to format dates.
     *
     * See {@link #DATE_FORMAT_DEFAULT} for default.
     *
     * @param dateFormat The date format to use for formatting dates as defined by the acquirer/merchant
     */
    public void setDateFormat(String dateFormat) {
        if (dateFormat != null) {
            this.dateFormat = dateFormat;
        }
    }

    /**
     * Get the time format to use for formatting time, as defined by the acquirer/merchant.
     *
     * This can be used with the Java SimpleDateFormat class to format time.
     *
     * See {@link #TIME_FORMAT_DEFAULT} for default.
     *
     * @return The time format to use for formatting time as defined by the acquirer/merchant
     */
    @NonNull
    public String getTimeFormat() {
        return timeFormat;
    }

    /**
     * Set the time format to use for formatting time, as defined by the acquirer/merchant.
     *
     * This can be used with the Java SimpleDateFormat class to format time.
     *
     * See {@link #TIME_FORMAT_DEFAULT} for default.
     *
     * @param timeFormat The time format to use for formatting time as defined by the acquirer/merchant
     */
    public void setTimeFormat(String timeFormat) {
        if (timeFormat != null) {
            this.timeFormat = timeFormat;
        }
    }

    /**
     * Get the primary currency represented as ISO-4217 code for the region/environment the device is operating in.
     *
     * This can be used by applications that need to dynamically retrieve the relevant primary currency.
     *
     * This is intended to be a more reliable and flexible alternative to Currency.getInstance(Locale.getDefault()).
     *
     * See {@link #PRIMARY_CURRENCY_DEFAULT} for default.
     *
     * @return The ISO-4217 primary currency code
     */
    @Nullable
    public String getPrimaryCurrency() {
        return primaryCurrency;
    }

    /**
     * Set the primary currency represented as ISO-4217 code for the region/environment the device is operating in.
     *
     * This can be used by applications that need to dynamically retrieve the relevant primary currency.
     *
     * This is intended to be a more reliable and flexible alternative to Currency.getInstance(Locale.getDefault()).
     *
     * See {@link #PRIMARY_CURRENCY_DEFAULT} for default.
     *
     * @param primaryCurrency The ISO-4217 primary currency code
     */
    public void setPrimaryCurrency(String primaryCurrency) {
        this.primaryCurrency = primaryCurrency;
    }

    /**
     * Get the primary language represented as ISO-639-1 code for the region/environment the device is operating in.
     *
     * This can be used by applications that need to dynamically retrieve the relevant primary language.
     *
     * This is intended to be a more reliable and flexible alternative to Locale.getDefault().getLanguage().
     *
     * See {@link #PRIMARY_LANGUAGE_DEFAULT} for default.
     *
     * @return The primary ISO-639-1 two letter currency code
     */
    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    /**
     * Set the primary language represented as ISO-639-1 code for the region/environment the device is operating in.
     *
     * This can be used by applications that need to dynamically retrieve the relevant primary language.
     *
     * This is intended to be a more reliable and flexible alternative to Locale.getDefault().getLanguage().
     *
     * See {@link #PRIMARY_LANGUAGE_DEFAULT} for default.
     *
     * @param primaryLanguage The primary ISO-639-1 two letter currency code
     */
    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    /**
     * Get the comms channel that is used for any communication between applications in AppFlow.
     *
     * This determines how clients communicate with FPS and how FPS in turn communicates with flow services.
     *
     * See {@link #COMMS_CHANNEL_DEFAULT} for default.
     *
     * @return The comms channel between AppFlow apps
     */
    public String getCommsChannel() {
        return commsChannel;
    }

    /**
     * Set the comms channel that is used for any communication between applications in AppFlow.
     *
     * This determines how clients communicate with FPS and how FPS in turn communicates with flow services.
     *
     * See {@link #COMMS_CHANNEL_DEFAULT} for default.
     *
     * @param commsChannel comms channel between AppFlow apps
     */
    public void setCommsChannel(String commsChannel) {
        this.commsChannel = commsChannel;
    }

    /**
     * Convenience function for retrieving a combined date and time formatter..
     *
     * The formatter uses a pattern of {@link #getDateFormat()} + " " + {@link #getTimeFormat()}.
     *
     * @return Date and time formatter
     */
    public SimpleDateFormat getDateTimeFormat() {
        return new SimpleDateFormat(String.format("%s %s", dateFormat, timeFormat), Locale.getDefault());
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static AppFlowSettings fromJson(String json) {
        return JsonConverter.deserialize(json, AppFlowSettings.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppFlowSettings that = (AppFlowSettings) o;
        return Objects.equals(dateFormat, that.dateFormat) &&
                Objects.equals(timeFormat, that.timeFormat) &&
                Objects.equals(primaryCurrency, that.primaryCurrency) &&
                Objects.equals(primaryLanguage, that.primaryLanguage) &&
                Objects.equals(commsChannel, that.commsChannel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateFormat, timeFormat, primaryCurrency, primaryLanguage, commsChannel);
    }
}
