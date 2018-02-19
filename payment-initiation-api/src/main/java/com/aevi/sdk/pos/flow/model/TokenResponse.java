package com.aevi.sdk.pos.flow.model;

import com.aevi.sdk.flow.model.Token;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.SendableId;

import io.reactivex.annotations.Nullable;

/**
 * Response object returned for {@link PaymentClient#generateCardToken()} requests
 */
public class TokenResponse extends SendableId {

    private final Token token;

    public TokenResponse(Token token) {
        this.token = token;
    }

    /**
     * Returns the token obtained from the customer card, if any.
     *
     * @return Token representing the token, or null if operation did not succeed.
     */
    @Nullable
    public Token getToken() {
        return token;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static TokenResponse fromJson(String json) {
        return JsonConverter.deserialize(json, TokenResponse.class);
    }
}
