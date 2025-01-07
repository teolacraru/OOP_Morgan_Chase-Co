package org.poo.main.commands;

import org.poo.main.User;
import org.poo.main.accounts.Account;

import java.util.List;

public class SetMinBalanceCommand implements Command {
    private final double amount;
    private final String accountIBAN;
    private final int timestamp;
    private final List<User> users;

    /*
    * Sets the minimum balance.
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
        accountToSet.setMinBalance(amount);
    }
}
