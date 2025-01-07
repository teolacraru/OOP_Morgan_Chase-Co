package org.poo.main.cards;

/**
 * Represents a card associated with an account.
 * Cards can have a type and status, such as "active", "frozen", or "warning".
 */
public class Card {
    private final String cardNumber; // The unique number of the card.
    private final String type; // The type of the card (e.g., "normal", "one-time").
    private String status; // The current status of the card.

    /**
     * Constructs a new card with the specified card number and type.
     * The card is initially set to an "active" status.
     *
     * @param cardNumber the unique number of the card.
     * @param type       the type of the card.
     */
    public Card(final String cardNumber, final String type) {
        this.cardNumber = cardNumber;
        this.status = "active"; // Default status
        this.type = type;
    }

    /**
     * Returns the unique number of the card.
     *
     * @return the card number.
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Returns the current status of the card.
     *
     * @return the card status ("active", "frozen", or "warning").
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the type of the card.
     *
     * @return the card type.
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the card's status to "frozen".
     */
    public void frozen() {
        status = "frozen";
    }

    /**
     * Updates the card's status to "warning".
     */
    public void warning() {
        status = "warning";
    }
}
