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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A class that represents a single receipt layout for a flow type and receipt type (customer/merchant).
 */
public class ReceiptLayout implements Jsonable {

    public enum ReceiptType {
        CUSTOMER,
        MERCHANT
    }

    private final String flowType;
    private final ReceiptType receiptType;
    private final String receiptTitle;
    private final boolean displayHeader, displayFooter, displayBaskets, displayExtras;
    private final String dateFormat;
    private final String timeFormat;
    private final String dateTimeFormat;

    private final Map<String, ReceiptLine[]> transactionSections;
    private final Map<String, ReceiptLine[]> extraSections;
    private final Map<String, String> labels;

    /**
     * Build a new receipt layout
     *
     * @param flowType       The flow type this layout should be used for
     * @param receiptType    The type of receipt this is customer or merchant
     * @param receiptTitle   The title to display on the receipt
     * @param displayHeader  If true this receipt will show header data if one has been setup by the merchant
     * @param displayBaskets If true this receipt will show baskets if there are any
     * @param displayExtras  If true this receipt will show all available extras sections. Usually setup by flow services
     * @param displayFooter  If true this receipt will show footer data if any has been setup by the merchant
     * @param dateFormat     The date format this layout should use
     * @param timeFormat     The time format this layout should use
     * @param dateTimeFormat The date time format this layout should use when date and time are shown together
     */
    public ReceiptLayout(
            @NonNull String flowType,
            @NonNull ReceiptType receiptType,
            @NonNull String receiptTitle,
            boolean displayHeader,
            boolean displayBaskets,
            boolean displayExtras,
            boolean displayFooter,
            @NonNull String dateFormat,
            @NonNull String timeFormat,
            @NonNull String dateTimeFormat) {
        this.flowType = flowType;
        this.receiptType = receiptType;
        this.receiptTitle = receiptTitle;
        this.displayHeader = displayHeader;
        this.displayBaskets = displayBaskets;
        this.displayExtras = displayExtras;
        this.displayFooter = displayFooter;
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
        this.dateTimeFormat = dateTimeFormat;
        this.labels = new HashMap<>();
        this.transactionSections = new HashMap<>();
        this.extraSections = new HashMap<>();
    }

    /**
     * The flow type this receipt layout is for
     *
     * @return A flow type
     */
    @NonNull
    public String getFlowType() {
        return flowType;
    }

    /**
     * The receipt type this receipt is for
     *
     * @return Customer or Merchant
     */
    @NonNull
    public ReceiptType getReceiptType() {
        return receiptType;
    }

    /**
     * This title of this receipt
     *
     * @return A plain text string
     */
    @NonNull
    public String getReceiptTitle() {
        return receiptTitle;
    }

    /**
     * If true this receipt should show header information if it is available
     *
     * @return True to display header data
     */
    public boolean shouldDisplayHeader() {
        return displayHeader;
    }

    /**
     * If true this receipt should show footer information if it is available
     *
     * @return True to display footer data
     */
    public boolean shouldDisplayFooter() {
        return displayFooter;
    }

    /**
     * If true this receipt should show basket information if it is available
     *
     * @return True to display basket data
     */
    public boolean shouldDisplayBaskets() {
        return displayBaskets;
    }

    /**
     * If true this receipt should show extra sections if they are available
     *
     * @return True to display extra section data
     */
    public boolean shouldDisplayExtras() {
        return displayExtras;
    }

    /**
     * The format string should use the date characters as defined by {@link SimpleDateFormat}
     *
     * @return The format that should be used by default to format dates
     */
    @NonNull
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * The format string should use the time characters as defined by {@link SimpleDateFormat}
     *
     * @return The format that should be used by default to format dates
     */
    @NonNull
    public String getTimeFormat() {
        return timeFormat;
    }

    /**
     * The format string should use the date and time characters as defined by {@link SimpleDateFormat}
     *
     * @return The format that should be used by default to format dates and times shown together
     */
    @NonNull
    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    /**
     * Add a general purpose label to be used on the receipt
     *
     * @param dataKey The data key the label should be stored as
     * @param label   The label text
     */
    public void addLabel(@NonNull String dataKey, @NonNull String label) {
        labels.put(dataKey, label);
    }

    /**
     * Add a specific transaction section to the receipt
     *
     * See <a href="https://github.com/AEVI-AppFlow/api-constants/blob/master/api-constants/src/main/java/com/aevi/sdk/flow/constants/config/ReceiptLabelKeys.java" target="_blank">
     * https://github.com/AEVI-AppFlow/api-constants/blob/master/api-constants/src/main/java/com/aevi/sdk/flow/constants/config/ReceiptLabelKeys.java</a> for section keys.
     *
     * @param sectionKey The key to store these lines as
     * @param lines      The receipt lines for the section
     */
    public void addTransactionSection(@NonNull String sectionKey, @NonNull ReceiptLine... lines) {
        transactionSections.put(sectionKey, lines);
    }

    /**
     * @return A set of all the transaction sections for this layout
     */
    @NonNull
    public Set<String> getTransactionSectionKeys() {
        return transactionSections.keySet();
    }

    /**
     * Add an extra sections to this receipt layout
     *
     * @param sectionKey The name of the extras section. Usually defined by a flow service e.g. loyalty
     * @param lines      The receipt lines for the section
     */
    public void addExtraSection(@NonNull String sectionKey, @NonNull ReceiptLine... lines) {
        extraSections.put(sectionKey, lines);
    }

    /**
     * @return The set of all extra sections
     */
    @NonNull
    public Set<String> getExtraSectionKeys() {
        return extraSections.keySet();
    }

    /**
     * Get a label value from its key
     *
     * @param key The key of the label
     * @return The label or empty String if not present
     */
    @NonNull
    public String getLabel(String key) {
        if (labels.containsKey(key)) {
            return labels.get(key);
        }
        return "";
    }

    /**
     * Get an extras section receipt lines if present
     *
     * @param sectionKey The section to obtain
     * @return The section receipt lines or null if not present
     */
    @Nullable
    public ReceiptLine[] getExtraSection(String sectionKey) {
        return extraSections.get(sectionKey);
    }

    /**
     * Get a transaction section receipt lines if present
     *
     * @param sectionKey The section to obtain
     * @return The section receipt lines or null if not present
     */
    @Nullable
    public ReceiptLine[] getTransactionSection(String sectionKey) {
        return transactionSections.get(sectionKey);
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static ReceiptLayout fromJson(String json) {
        return JsonConverter.deserialize(json, ReceiptLayout.class);
    }
}
