package org.poo.main.commands;

import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.accounts.AccountFactory;
import org.poo.utils.Utils;

import java.util.List;

/**
 * Command to add a new account for a user.
 * Supports creating "classic" and "savings" accounts with specified parameters.
 */
public class AddAccountCommand implements Command {
    private final String email;
    private final String currency;
    private final String accountType;
    private final int timestamp;
    private final double interestRate;
    private final List<User> users;

    /**
     * Constructs the AddAccountCommand.
     *
     * @param email        the email of the user for whom the account will be created.
     * @param currency     the currency of the account.
     * @param accountType  the type of the account ("classic" or "savings").
     * @param timestamp    the time of account creation.
     * @param interestRate the interest rate for savings accounts.
     * @param users        the list of users to search for the target user.
     */
    public AddAccountCommand(final String email,
                             final String currency,
                             final String accountType,
                             final int timestamp,
                             final double interestRate,
                             final List<User> users) {
        this.email = email;
        this.currency = currency;
        this.accountType = accountType;
        this.timestamp = timestamp;
        this.interestRate = interestRate;
        this.users = users;
    }

    /**
     * Executes the command to create a new account for the user.
     * Generates an IBAN, initializes the account with a balance of 0,
     * and adds a corresponding transaction to the user's and account's transaction history.
     */
    @Override
    public void execute() {
        User user = null;
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                user = u;
                break;
            }
        }

        if (user == null) {
            throw new IllegalArgumentException("User not found: " + email);
        }

        String iban = Utils.generateIBAN();
        Account account = AccountFactory.createAccount(accountType, iban, currency,
                interestRate, user);
        account.setBalance(0.0);
        user.addAccount(account);

        Transaction transaction = Transaction.addAccountTransaction(
                timestamp,
                "New account created",
                iban,
                currency
        );
        user.addTransaction(transaction);

        if ("classic".equals(accountType)) {
            account.addTransaction(transaction);
        }
    }
}
