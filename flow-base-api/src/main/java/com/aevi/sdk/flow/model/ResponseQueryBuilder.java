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

/**
 * Builder used to create {@link ResponseQuery} objects
 */
public class ResponseQueryBuilder {

    private long startDate;
    private long endDate;
    private String flowName;
    private String requestId;
    private String flowType;
    private int maxResults = 100;

    /**
     * Setup a new query builder
     */
    public ResponseQueryBuilder() {
    }

    /**
     * Optionally restrict this query to responses for the given flow name
     *
     * @param flowName The flow name to restrict to
     * @return this builder
     */
    public ResponseQueryBuilder withFlowName(String flowName) {
        this.flowName = flowName;
        return this;
    }

    /**
     * Optionally restrict this query to responses for the given flow type
     *
     * @param flowType The flow type to restrict to
     * @return this builder
     */
    public ResponseQueryBuilder withFlowType(String flowType) {
        this.flowType = flowType;
        return this;
    }

    /**
     * Optionally restrict this query to responses within a given date range
     *
     * The start date is the earliest date to search for and the end date is the latest. Both times are in milliseconds since epoch.
     *
     * Both parameters must be set and the startDate must be less than the endDate
     *
     * @param startDate The earliest date to collect responses for
     * @param endDate   The last date to collect responses for
     * @return this builder
     */
    public ResponseQueryBuilder withDateRange(long startDate, long endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    /**
     * Optionally restrict this query to a specific requestId
     *
     * If this is set all other parameters set in this builder will be ignored
     *
     * @param requestId The requestId restrict to
     * @return this builder
     */
    public ResponseQueryBuilder withRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    /**
     * This field can be used to change the number of results returned in this query. By default it is set to a maximum of 100
     *
     * @param maxResults The maximum number of responses to return
     * @return this builder
     */
    public ResponseQueryBuilder withMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public ResponseQuery build() {

        if (endDate > 0 && startDate <= 0) {
            throw new IllegalArgumentException("Start date must be set for date range");
        }

        if (startDate > 0 && endDate <= 0) {
            throw new IllegalArgumentException("End date must be set for date range");
        }

        if (endDate > 0 && startDate >= endDate) {
            throw new IllegalArgumentException("Invalid parameters, start date must be earlier (lower) than end date");
        }

        return new ResponseQuery(requestId, flowName, flowType, startDate, endDate, maxResults);
    }
}