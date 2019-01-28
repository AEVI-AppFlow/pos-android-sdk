package com.aevi.sdk.flow.model;

import com.aevi.sdk.flow.BaseApiClient;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

/**
 * An internal object used to serialise the query data for
 * Response requests via {@link BaseApiClient#queryResponses(ResponseQuery)}
 */
public final class ResponseQuery implements Jsonable {

    private String responseType;
    private String flowName;
    private String flowType;
    private long startDate;
    private long endDate;
    private int maxResults;

    /**
     * @param flowName   of the flow to get responses for. Can be set to null if not required
     * @param flowType   The type of flow to get responses for. Can be set to null if not required
     * @param startDate  The first/start date to use to restrict the results to
     * @param endDate    The last/end date to use to restrict the results to
     * @param maxResults The maximum number of responses to return
     */
    ResponseQuery(String flowName, String flowType, long startDate, long endDate, int maxResults) {
        this.flowName = flowName;
        this.flowType = flowType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxResults = maxResults;
    }

    /**
     * An internal method used by this API to set the response type required for this query.
     *
     * FOR INTERNAL USE ONLY: This method should not be used by external classes
     *
     * @param responseType The class name of the response type to expect for this query
     */
    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getFlowName() {
        return flowName;
    }

    public String getFlowType() {
        return flowType;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public int getMaxResults() {
        return maxResults;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static ResponseQuery fromJson(String json) {
        return JsonConverter.deserialize(json, ResponseQuery.class);
    }
}
