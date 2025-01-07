package org.poo.main.commands;

import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.cards.Card;
import org.poo.main.cards.CardFactory;

import java.util.List;

/**
 * Command for creating a card linked to a user's account.
 */
public class CreateCardCommand implements Command {

    private final String email;
    private final String accountIBAN;
    private final String cardType;
    private final int timestamp;
    private final List<User> users;

    /**
     * Constructor for CreateCardCommand.
     *
     * @param email       the user's email.
     * @param accountIBAN the IBAN of the account linked to the card.
     * @param cardType    the type of card (e.g., normal, one-time).
     * @param timestamp   the time when the card is created.
     * @param users       the list of users in the system.
     */
    public CreateCardCommand(final String email, final String accountIBAN, final String cardType,
                             final int timestamp, final List<User> users) {
        this.email = email;
        this.accountIBAN = accountIBAN;
        this.cardType = cardType;
        this.timestamp = timestamp;
        this.users = users;
    }

    /**
     * Executes the card creation command.
     */
    @Override
    public void execute() {
        User user = null;
        for (User u : users) {
            //System.out.println(u.getEmail() + " " + email);
            if (u.getEmail().equals(email)) {
                user = u;
                break;
            }
        }
        if (user == null) {
            return;
        }

        Account account = null;
        for (Account a : user.getAccounts()) {
            if (a.getIBAN().equals(accountIBAN)) {
                account = a;
                break;
            }
        }
        if (account == null) {
            throw new IllegalArgumentException("Account.java not found: " + accountIBAN);
        }
        Card card = CardFactory.createCard(cardType);
        account.addCard(card);
        Transaction transaction = Transaction.createCardTransaction(
                timestamp, "New card created", accountIBAN, card.getCardNumber(), user.getEmail()
        );
        account.getOwner().addTransaction(transaction);
        account.addTransaction(transaction);
    }


}
