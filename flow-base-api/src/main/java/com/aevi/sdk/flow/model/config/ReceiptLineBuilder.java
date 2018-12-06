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

import com.aevi.sdk.flow.model.AdditionalData;

/**
 * A builder that can be used to create valid {@link ReceiptLine} instances before they are added to a {@link ReceiptLayout}
 */
public class ReceiptLineBuilder {

    private String dataKey = null;
    private String label = null;
    private int emptyLinesAfter = 0;
    private Character fillWithChar = null;
    private String alignment = null;
    private String underline = null;
    private String fontStyle = null;
    private boolean allCapitals = false;

    public ReceiptLineBuilder() {

    }

    /**
     * If this line has some information to be retrieved from an {@link AdditionalData} object somewhere in the transaction data.
     *
     * The correct additional data is inferred from the name of the section this line is placed in
     *
     * @param dataKey The data key to find
     * @return This builder
     */
    public ReceiptLineBuilder withDataKey(String dataKey) {
        this.dataKey = dataKey;
        return this;
    }

    /**
     * A label to use for this line.
     *
     * For lines without any data then this may be the only field set for the line.
     *
     * @param label The label
     * @return This builder
     */
    public ReceiptLineBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    /**
     * If the rest of this line should be filled with a char. Useful for signature lines etc.
     *
     * @param fillWithChar The char to fill the line with
     * @return This builder
     */
    public ReceiptLineBuilder withFillWithChar(char fillWithChar) {
        this.fillWithChar = fillWithChar;
        return this;
    }

    /**
     * For lines consisting of only a label then it can be aligned by setting this value
     *
     * See <a href="https://github.com/Aevi-UK/android-pos-print-api/blob/develop/print-api/src/main/java/com/aevi/print/model/Alignment.java" target="_blank">
     * https://github.com/Aevi-UK/android-pos-print-api/blob/develop/print-api/src/main/java/com/aevi/print/model/Alignment.java</a>
     *
     * @param alignment A value indicating how this label should be aligned
     * @return This builder
     */
    public ReceiptLineBuilder withAlignment(String alignment) {
        this.alignment = alignment;
        return this;
    }

    /**
     * If this receipt line should be underlined
     *
     * See <a href="https://github.com/Aevi-UK/android-pos-print-api/blob/develop/print-api/src/main/java/com/aevi/print/model/Underline.java" target="_blank">
     * https://github.com/Aevi-UK/android-pos-print-api/blob/develop/print-api/src/main/java/com/aevi/print/model/Underline.java</a>
     *
     * @param underline A value indicating if this label should be underlined
     * @return This builder
     */
    public ReceiptLineBuilder withUnderline(String underline) {
        this.underline = underline;
        return this;
    }

    /**
     * An indication of the style to apply to the line
     *
     * See <a href="https://github.com/Aevi-UK/android-pos-print-api/blob/develop/print-api/src/main/java/com/aevi/print/model/FontStyle.java" target="_blank">
     * https://github.com/Aevi-UK/android-pos-print-api/blob/develop/print-api/src/main/java/com/aevi/print/model/FontStyle.java</a>
     *
     * @param fontStyle A value indicating how this line should be styles
     * @return This builder
     */
    public ReceiptLineBuilder withFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
        return this;
    }

    /**
     * The number of empty lines to append after this one is added to the receipt
     *
     * @param emptyLinesAfter A number
     * @return This builder
     */
    public ReceiptLineBuilder withEmptyLinesAfter(int emptyLinesAfter) {
        this.emptyLinesAfter = emptyLinesAfter;
        return this;
    }

    /**
     * If this line contains a data key then setting this flag will cause the data to be converted to uppercase
     *
     * @param allCapitals True to set the data value to uppercase
     * @return This builder
     */
    public ReceiptLineBuilder withAllCapitals(boolean allCapitals) {
        this.allCapitals = allCapitals;
        return this;
    }

    /**
     * Build the receipt line
     *
     * @return The receipt line
     */
    public ReceiptLine build() {
        return new ReceiptLine(dataKey, label, emptyLinesAfter, fillWithChar, alignment, underline, fontStyle, allCapitals);
    }
}
