package com.aevi.sdk.pos.flow.model;

import com.aevi.sdk.flow.model.BaseModel;
import com.aevi.util.json.JsonConverter;

import java.util.UUID;

import android.support.annotation.NonNull;

/**
 * Contains card details and outcome from payment card reading.
 */
public class CardResponse extends BaseModel {

    private final Result result;
    private final Card card;
    private String paymentServiceId;

    public CardResponse(Card card) {
        super(UUID.randomUUID().toString());
        this.card = card != null ? card : Card.getEmptyCard();
        this.result = Result.SUCCESS;
    }

    public CardResponse(Result result) {
        super(UUID.randomUUID().toString());
        this.result = result;
        this.card = Card.getEmptyCard();
    }

    /**
     * Get the result of the card reading.
     *
     * @return result of card reading.
     */
    @NonNull
    public Result getResult() {
        return result;
    }

    public void setPaymentServiceId(String paymentServiceId) {
        this.paymentServiceId = paymentServiceId;
    }

    /**
     * Get the id of the payment service used to read the card.
     *
     * @return The id of the payment service that read the card.
     */
    public String getPaymentServiceId() {
        return paymentServiceId;
    }

    /**
     * Get the card data.
     *
     * @return The card data.
     */
    @NonNull
    public Card getCard() {
        return card;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public enum Result {
        SUCCESS,
        DECLINED,
        ABORTED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CardResponse that = (CardResponse) o;

        if (result != that.result) return false;
        if (card != null ? !card.equals(that.card) : that.card != null) return false;
        return paymentServiceId != null ? paymentServiceId.equals(that.paymentServiceId) : that.paymentServiceId == null;
    }

    @Override
    public int hashCode() {
        int result1 = super.hashCode();
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (card != null ? card.hashCode() : 0);
        result1 = 31 * result1 + (paymentServiceId != null ? paymentServiceId.hashCode() : 0);
        return result1;
    }
}
