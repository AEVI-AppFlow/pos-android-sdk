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

import android.support.annotation.Nullable;
import com.aevi.sdk.flow.model.AdditionalData;

/**
 * A representation of a line to be printed on a receipt.
 *
 * Usually the line will consist of just a label or a label and some data displayed in left/right columns
 *
 * The line can consist of:
 * <ul>
 * <li>A label - usually just text to display on the line which can be aligned left, right, centre</li>
 * <li>A data key - a key to find some data in the transaction data</li>
 * <li>Num of empty lines - a number of empty/blank lines to append after this one</li>
 * <li>Fill with char - a character to fill the remainder of the line with. Useful for signatures</li>
 * <li>Alignment, underline and font style - various styling options</li>
 * </ul>
 */
public class ReceiptLine {

    private final String dataKey;
    private final String label;
    private final int emptyLinesAfter;
    private final Character fillWithChar;
    private final String alignment;
    private final String underline;
    private final String fontStyle;

    ReceiptLine(@Nullable String dataKey, @Nullable String label,
                int emptyLinesAfter, @Nullable Character fillWithChar,
                @Nullable String alignment,
                @Nullable String underline,
                @Nullable String fontStyle) {
        this.dataKey = dataKey;
        this.label = label;
        this.emptyLinesAfter = emptyLinesAfter;
        this.fillWithChar = fillWithChar;
        this.alignment = alignment;
        this.underline = underline;
        this.fontStyle = fontStyle;
    }

    /**
     * The name of a data key to get some information from the transaction.
     *
     * Depending on the section of the receipt this line is in will determine which {@link AdditionalData} object the data will be read from.
     *
     * @return The data key
     */
    @Nullable
    public String getDataKey() {
        return dataKey;
    }

    /**
     * A String to display on the receipt
     *
     * @return The text
     */
    @Nullable
    public String getLabel() {
        return label;
    }

    /**
     * The number of empty lines to append after this one on the receipt
     *
     * @return A number
     */
    public int getEmptyLinesAfter() {
        return emptyLinesAfter;
    }

    /**
     * For simple label lines this char will be used to fill out the rest of the line after the label
     *
     * @return The character to fill with or null if not required
     */
    @Nullable
    public Character fillWithChar() {
        return fillWithChar;
    }

    /**
     * For simple label lines this alignment value will be used to align the text
     *
     * See <a href="https://github.com/Aevi-UK/android-pos-print-api/blob/develop/print-api/src/main/java/com/aevi/print/model/Alignment.java">
     * https://github.com/Aevi-UK/android-pos-print-api/blob/develop/print-api/src/main/java/com/aevi/print/model/Alignment.java</a> for values.
     *
     * @return The alignment value or null
     */
    @Nullable
    public String getAlignment() {
        return alignment;
    }


    /**
     * Whether or not this line should be underlined
     *
     * See <a href="https://github.com/Aevi-UK/android-pos-print-api/blob/develop/print-api/src/main/java/com/aevi/print/model/Underline.java">
     * https://github.com/Aevi-UK/android-pos-print-api/blob/develop/print-api/src/main/java/com/aevi/print/model/Underline.java</a> for values.
     *
     * @return The underline type or null
     */
    @Nullable
    public String getUnderline() {
        return underline;
    }

    /**
     * Whether or not this line should be styled
     *
     * See <a href="https://github.com/Aevi-UK/android-pos-print-api/blob/develop/print-api/src/main/java/com/aevi/print/model/FontStyle.java">
     * https://github.com/Aevi-UK/android-pos-print-api/blob/develop/print-api/src/main/java/com/aevi/print/model/FontStyle.java</a> for values.
     *
     * @return The font style or null
     */
    @Nullable
    public String getFontStyle() {
        return fontStyle;
    }

    /**
     * @return True if this line has both a label and some data from a key to display
     */
    public boolean hasLabelAndData() {
        // empty label is valid
        if (label != null && dataKey != null && !dataKey.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @return True if this line has some data from a key to display
     */
    public boolean hasData() {
        if (dataKey != null && !dataKey.isEmpty()) {
            return true;
        }
        return false;
    }
}

