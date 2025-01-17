package org.poo.main.commands;

import org.poo.main.User;
import org.poo.main.accounts.Account;

import java.util.List;

/**
 * Command to add funds to a specific account.
 * Finds the account by IBAN and updates its balance.
 */
public class AddFundsCommand implements Command {
    private final String accountIBAN;
    private final double amount;
    private final int timestamp;
    private final List<User> users;

    /**
     * Constructs the AddFundsCommand.
     *
     * @param accountIBAN the IBAN of the account to add funds to.
     * @param amount      the amount to be added to the account.
     * @param timestamp   the time of the fund addition.
     * @param users       the list of users to search for the account.
     */
    public AddFundsCommand(final String accountIBAN,
                           final double amount, final int timestamp,
                           final List<User> users) {
        this.accountIBAN = accountIBAN;
        this.amount = amount;
        this.timestamp = timestamp;
        this.users = users;
    }

    /**
     * Executes the command to add funds to the account.
     */
    @Override
    public void execute() {
        Account account = null;

        for (User user : users) {
            for (Account acc : user.getAccounts()) {
                if (acc.getIBAN().equals(accountIBAN)) {
                    account = acc;
                    break;
                }
            }
            if (account != null) {
                break;
            }
        }

        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountIBAN);
        }

        account.setBalance(account.getBalance() + amount);
//        if(timestamp == 7 ){
//            System.out.println(account.getOwner().getFirstName() + " " + account.getBalance());
//        }
    }
}
