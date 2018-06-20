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

package com.aevi.sdk.flow.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

/**
 * Token that can be used to identify an entity, typically a customer.
 *
 * How the value is generated is bespoke to the application that fulfilled the request.
 */
public class Token implements Jsonable {

    private final String value;
    private final String source;
    private final String algorithm;
    private String sourceAppId;

    // Default constructor for deserialisation
    Token() {
        this("", "", null);
    }

    public Token(String value, String source, String algorithm) {
        this.value = value;
        this.source = source;
        this.algorithm = algorithm;
    }

    public Token(String value, String source) {
        this(value, source, null);
    }

    /**
     * Get the token value.
     *
     * @return The token value
     */
    @NonNull
    public String getValue() {
        return value;
    }

    /**
     * The source application that generated the token.
     *
     * @return The source application that generated the token
     */
    @NonNull
    public String getSourceAppId() {
        return sourceAppId;
    }

    /**
     * For internal use.
     *
     * @param sourceAppId App id
     */
    public void setSourceAppId(String sourceAppId) {
        this.sourceAppId = sourceAppId;
    }

    /**
     * Get the source of what was used to generate this token.
     *
     * An example is "card".
     *
     * @return The source of the token, or null
     */
    @Nullable
    public String getSource() {
        return source;
    }

    /**
     * Get the algorithm used to generate this token.
     *
     * @return The algorithm used to generate this token, or null
     */
    @Nullable
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", source='" + source + '\'' +
                ", algorithm='" + algorithm + '\'' +
                ", sourceAppId='" + sourceAppId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (value != null ? !value.equals(token.value) : token.value != null) return false;
        if (source != null ? !source.equals(token.source) : token.source != null) return false;
        if (algorithm != null ? !algorithm.equals(token.algorithm) : token.algorithm != null) return false;
        return sourceAppId != null ? sourceAppId.equals(token.sourceAppId) : token.sourceAppId == null;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (algorithm != null ? algorithm.hashCode() : 0);
        result = 31 * result + (sourceAppId != null ? sourceAppId.hashCode() : 0);
        return result;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }
}
