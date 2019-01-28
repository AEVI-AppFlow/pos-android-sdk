package com.aevi.sdk.flow.model;

/**
 * Builder used to create {@link ResponseQuery} objects
 */
public class ResponseQueryBuilder {

    private final long startDate;
    private final long endDate;

    private String flowName;
    private String flowType;
    private int maxResults = 100;

    /**
     * Setup this query builder with an initial start and end date range
     *
     * The start date is the earliest date to search for and the end date is the latest. Both times are in milliseconds since epoch.
     *
     * Both parameters must be set and the startDate must be less than the endDate
     *
     * @param startDate The earliest date to search for
     * @param endDate   The latest date to search for
     */
    public ResponseQueryBuilder(long startDate, long endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
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

        if (startDate <= 0) {
            throw new IllegalArgumentException("Start date must be set for date range");
        }

        if (endDate <= 0) {
            throw new IllegalArgumentException("End date must be set for date range");
        }

        if (startDate >= endDate) {
            throw new IllegalArgumentException("Invalid parameters, start date must be earlier (lower) than end date");
        }

        return new ResponseQuery(flowName, flowType, startDate, endDate, maxResults);
    }
}