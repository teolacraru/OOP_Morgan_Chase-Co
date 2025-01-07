package org.poo.main.commands;

import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.accounts.SavingsAccount;

import java.util.List;

public class ChangeInterestRateCommand implements Command {
    private final int timestamp;
    private final double newInterestRate;
    private final String accountIban;
    private final List<User> users;

    /**
     * Constructs a ChangeInterestRateCommand.
     *
     * @param timestamp       the time the command is executed.
     * @param newInterestRate the new interest rate to be set.
     * @param accountIban     the IBAN of the account.
     * @param users           the list of users to search for the account.
     */
    public ChangeInterestRateCommand(final int timestamp, final double newInterestRate,
                                     final String accountIban, final List<User> users) {
        this.timestamp = timestamp;
        this.newInterestRate = newInterestRate;
        this.accountIban = accountIban;
        this.users = users;
    }

    /**
     * Executes the command to update the interest rate of the specified account.
     * Throws exceptions if the account is not found or is not a savings account.
     */
    @Override
    public void execute() {
        Account targetAccount = null;

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(accountIban)) {
                    targetAccount = account;
                    break;
                }
            }
        }

        if (targetAccount == null) {
            throw new IllegalArgumentException("Account not found for IBAN: " + accountIban);
        }

        if (!(targetAccount instanceof SavingsAccount savingsAccount)) {
            throw new IllegalArgumentException("This is not a savings account");
        }

        savingsAccount.setInterestRate(newInterestRate);
        Transaction transaction = Transaction.
                addAccountTransaction(timestamp, "Interest rate of the account changed to "
                        + newInterestRate, null, null);
        savingsAccount.getOwner().addTransaction(transaction);
    }
}
