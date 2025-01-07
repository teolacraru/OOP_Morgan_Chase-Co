package org.poo.main.commands;

import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.accounts.SavingsAccount;

import java.util.List;

/**
 * Command to add interest to a savings account.
 */
public class AddInterestCommand implements Command {
    private static final double PERCENTAGE_FACTOR = 100.0; // Avoid magic number
    private final String accountIBAN;
    private final int timestamp;
    private final List<User> users;

    /**
     * Constructs an AddInterestCommand.
     *
     * @param accountIBAN the IBAN of the account to add interest to.
     * @param timestamp   the time of interest addition.
     * @param users       the list of users to search for the account.
     */
    public AddInterestCommand(final String accountIBAN,
                              final int timestamp, final List<User> users) {
        this.accountIBAN = accountIBAN;
        this.timestamp = timestamp;
        this.users = users;
    }

    /**
     * Executes the command to add interest to the specified account.
     * Throws exceptions if the account is not found or is not a savings account.
     */
    @Override
    public void execute() {
        Account targetAccount = null;

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(accountIBAN)) {
                    targetAccount = account;
                    break;
                }
            }
            if (targetAccount != null) {
                break;
            }
        }

        if (targetAccount == null) {
            throw new IllegalArgumentException("Account not found for IBAN: " + accountIBAN);
        }

        if (!(targetAccount instanceof SavingsAccount savingsAccount)) {
            throw new IllegalArgumentException("This is not a savings account");
        }

        double interestRate = savingsAccount.getInterestRate();
        double interestAmount = targetAccount.getBalance() * (interestRate / PERCENTAGE_FACTOR);

        targetAccount.setBalance(targetAccount.getBalance() + interestAmount);

    }
}
