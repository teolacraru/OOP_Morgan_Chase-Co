package org.poo.main.commands;

import org.poo.main.User;
import org.poo.main.accounts.Account;

import java.util.List;

/**
 * Command to set a minimum balance for an account.
 * This class is not designed for extension and should be treated as final.
 */
public final class SetMinBalanceCommand implements Command {
    private final double amount;
    private final String accountIBAN;
    private final int timestamp;
    private final List<User> users;

    /**
     * Constructs a SetMinBalanceCommand.
     *
     * @param amount     the minimum balance to set
     * @param accountIBAN the IBAN of the account
     * @param timestamp  the timestamp of the command
     * @param users      the list of users
     */
    public SetMinBalanceCommand(final double amount,
                                final String accountIBAN,
                                final int timestamp,
                                final List<User> users) {
        this.amount = amount;
        this.accountIBAN = accountIBAN;
        this.timestamp = timestamp;
        this.users = users;
    }

    /**
     * Executes the command to set the minimum balance for the specified account.
     * Ensures the account is identified and updated correctly.
     *
     * @throws IllegalArgumentException if the account is not found
     */
    @Override
    public void execute() {
        Account accountToSet = null;

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(accountIBAN)) {
                    accountToSet = account;
                    break;
                }
            }
        }

        if (accountToSet == null) {
            throw new IllegalArgumentException("Account with IBAN " + accountIBAN + " not found.");
        }

        accountToSet.setMinBalance(amount);
    }
}
