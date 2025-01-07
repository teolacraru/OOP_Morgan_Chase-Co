package org.poo.main.cards;

import org.poo.utils.Utils;

public class CardFactory {
    /*
    * Creates a card based on the type
    */
    public static Card createCard(String type) {
        String cardNumber = Utils.generateCardNumber();
        switch (type) {
            case "one-time":
                return new OneTimeCard(cardNumber);
            case "normal":
                return new Card(cardNumber, "normal");
        }
        return null;
    }
}
