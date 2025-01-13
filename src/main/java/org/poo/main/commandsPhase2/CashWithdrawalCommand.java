package org.poo.main.commandsPhase2;

import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.cards.Card;
import org.poo.main.commands.Command;

import java.util.List;

public class CashWithdrawalCommand implements Command {
    private final String cardNumber;
    private final double amount;
    private final String email;
    private final String location;
    private final int timestamp;
    private final List<User> users;

    /**
     * Constructor for CashWithdrawalCommand.
     *
     * @param cardNumber the card number used for withdrawal.
     * @param amount     the amount to withdraw.
     * @param email      the email of the user.
     * @param location   the ATM location.
     * @param timestamp  the time of the transaction.
     * @param users      the list of users.
     */
    public CashWithdrawalCommand(final String cardNumber, final double amount, final String email,
                                 final String location, final int timestamp, final List<User> users) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.email = email;
        this.location = location;
        this.timestamp = timestamp;
        this.users = users;
    }

    public void execute() {
        User user = null;
        Account account = null;
        Card card = null;

        for (User u : users) {
            if (u.getEmail().equals(email)) {
                user = u;
                for (Account acc : u.getAccounts()) {
                    for (Card c : acc.getCards()) {
                        if (c.getCardNumber().equals(cardNumber)) {
                            card = c;
                            account = acc;
                            break;
                        }
                    }
                }
            }
        }
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (card == null) {
            throw new IllegalArgumentException("Card not found");
        }
        if (card.getStatus().equals("frozen")) {
            throw new IllegalArgumentException("The card is frozen");
        }
        if (amount > account.getBalance()) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        if (account.getBalance() - amount < account.getMinBalance()) {
            throw new IllegalArgumentException("Cannot perform payment due to a minimum balance being set");
        }



    }

}
