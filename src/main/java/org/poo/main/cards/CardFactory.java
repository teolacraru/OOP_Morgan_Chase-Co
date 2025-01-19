package org.poo.main.cards;

import org.poo.utils.Utils;

/**
 * Factory class for creating Card objects.
 */
public class CardFactory {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private CardFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a card based on the provided type.
     *
     * @param type the type of card to create ("one-time" or "normal").
     * @return a new Card object of the specified type.
     * @throws IllegalArgumentException if the card type is unknown.
     */
    public static Card createCard(final String type) {
        String cardNumber = Utils.generateCardNumber();
        switch (type) {
            case "one-time":
                return new OneTimeCard(cardNumber);
            case "normal":
                return new Card(cardNumber, "normal");
            default:
                throw new IllegalArgumentException("Unknown card type: " + type);
        }
    }
}
