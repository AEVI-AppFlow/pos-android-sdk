package com.aevi.sdk.pos.flow.sample;


import com.aevi.sdk.flow.constants.CardDataKeys;
import com.aevi.sdk.flow.constants.CardEntryMethods;
import com.aevi.sdk.flow.constants.CardNetworks;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.pos.flow.model.Card;
import com.aevi.sdk.pos.flow.model.CardBuilder;

public class CardProducer {

    private static final String VISA_PAN = CardBuilder.maskPan("4612345678123456");
    private static final String MASTERCARD_PAN = CardBuilder.maskPan("5312345678123456");
    private static final String AMEX_PAN = CardBuilder.maskPan("3712345678123456");
    private static final String EXPIRY_DATE = "2506";

    private static final String VISA_AID = "A0000000031010";
    private static final String MASTERCARD_AID = "A0000000041010";
    private static final String AMEX_AID = "A0000000250000";
    private static final String[] LANGUAGES = new String[]{"en", "de", "fr"};

    private static final String WE_BUILT_THIS_CARD_KEY = "cardProducedInSample"; // We built this card on rock'n'roll...

    public static Card getDefaultCard() {
        return buildCard(CardNetworks.CARD_NETWORK_VISA, true, false, false);
    }

    public static Card buildCard(String cardNetwork, boolean includeToken, boolean includeCardholder, boolean includeEmvDetails) {
        String maskedPan = getPanForNetwork(cardNetwork);
        Customer customer = CustomerProducer.getDefaultCustomer("cardProducer");
        String cardholderName = includeCardholder ? customer.getFullName() : null;

        CardBuilder cardBuilder = new CardBuilder()
                .withMaskedPan(maskedPan)
                .withExpiryDate(EXPIRY_DATE)
                .withCardholderName(cardholderName)
                .withAdditionalData(CardDataKeys.NETWORK, cardNetwork)
                .withAdditionalData(CardDataKeys.ENTRY_METHOD, CardEntryMethods.CARD_ENTRY_METHOD_INSERT);

        if (includeToken) {
            cardBuilder.withCardToken(customer.getTokens().get(0));
        }
        if (includeEmvDetails) {
            cardBuilder
                    .withAdditionalData(CardDataKeys.AID, getAidForNetwork(cardNetwork))
                    .withAdditionalData(CardDataKeys.LANGUAGES, LANGUAGES);
        }
        cardBuilder.withAdditionalData(WE_BUILT_THIS_CARD_KEY, true);

        return cardBuilder.build();
    }

    public static boolean cardWasProducedHere(Card card) {
        return card != null && card.getAdditionalData().hasData(WE_BUILT_THIS_CARD_KEY);
    }

    private static String getPanForNetwork(String cardNetwork) {
        switch (cardNetwork) {
            case CardNetworks.CARD_NETWORK_VISA:
                return VISA_PAN;
            case CardNetworks.CARD_NETWORK_MASTERCARD:
                return MASTERCARD_PAN;
            case CardNetworks.CARD_NETWORK_AMEX:
                return AMEX_PAN;
            default:
                return VISA_PAN;
        }
    }

    private static String getAidForNetwork(String cardNetwork) {
        cardNetwork = cardNetwork.toLowerCase();
        switch (cardNetwork) {
            case CardNetworks.CARD_NETWORK_VISA:
                return VISA_AID;
            case CardNetworks.CARD_NETWORK_MASTERCARD:
                return MASTERCARD_AID;
            case CardNetworks.CARD_NETWORK_AMEX:
                return AMEX_AID;
            default:
                return VISA_AID;
        }
    }
}
