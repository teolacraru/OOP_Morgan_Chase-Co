package org.poo.main.commands;

import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.cards.Card;

import java.util.List;

/**
 * Command to delete a card from an account.
 */
public class DeleteCardCommand implements Command {
    private final List<User> users;
    private final String cardNumber;
    private final int timestamp;

    /**
     * Constructor for DeleteCardCommand.
     *
     * @param cardNumber the card number to delete.
     * @param timestamp  the timestamp of the command.
     * @param users      the list of users.
     */
    public DeleteCardCommand(final String cardNumber,
                             final int timestamp,
                             final List<User> users) {
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;
        this.users = users;
    }

    /**
     * Executes the card deletion command.
     */
    @Override
    public void execute() {
        Card cardToDelete = null;
        Account accountToModify = null;

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (cardNumber.equals(card.getCardNumber())) {
                        cardToDelete = card;
                        accountToModify = account;
                        break;
                    }
                }
                if (cardToDelete != null) {
                    break;
                }
            }
            if (cardToDelete != null) {
                break;
            }
        }

        if (cardToDelete == null) {
            return;
        }

        accountToModify.removeCard(cardNumber);
        Transaction transaction = Transaction.
                deleteCardTransaction(accountToModify.getIBAN(), timestamp, cardNumber,
                        accountToModify.getOwner().email, "The card has been destroyed");
        accountToModify.getOwner().addTransaction(transaction);
    }
}
