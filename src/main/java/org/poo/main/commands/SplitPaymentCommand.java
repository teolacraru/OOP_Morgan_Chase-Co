package org.poo.main.commands;

import org.poo.main.CurrencyConverter;
import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles splitting a payment across multiple accounts.
 */
public class SplitPaymentCommand implements Command {
    private final int timestamp;
    private final String currency;
    private final double amount;
    private final List<String> accountIbans;
    private final List<User> users;
    private final CurrencyConverter currencyConverter;

    /**
     * Constructor for SplitPaymentCommand.
     *
     * @param timestamp        the timestamp of the command.
     * @param currency         the currency of the payment.
     * @param amount           the amount to be split.
     * @param accountIbans     the list of account IBANs involved.
     * @param users            the list of users in the system.
     * @param currencyConverter the currency converter utility.
     */
    public SplitPaymentCommand(final int timestamp,
                               final String currency,
                               final double amount,
                               final List<String> accountIbans,
                               final List<User> users,
                               final CurrencyConverter currencyConverter) {
        this.timestamp = timestamp;
        this.currency = currency;
        this.amount = amount;
        this.accountIbans = accountIbans;
        this.users = users;
        this.currencyConverter = currencyConverter;
    }

    /**
     * Executes the split payment command.
     */
    public void execute() {
        List<Account> accounts = new ArrayList<>();
        for (String iban : accountIbans) {
            for (User u : users) {
                for (Account a : u.getAccounts()) {
                    if (a.getIBAN().equals(iban)) {
                        accounts.add(a);
                        break;
                    }
                }
            }
        }

        int number = accounts.size();
        double splitAmount = amount / number;
        boolean ok = true;
        String guiltyAccount = null;
        for (Account account : accounts) {
            double convertedAmount =
                    currencyConverter.convert(splitAmount, currency, account.getCurrency());
            if (account.getBalance() < convertedAmount) {
                ok = false;
                guiltyAccount = account.getIBAN();
                //break;
            }
        }
        if (!ok) {
            for (Account account : accounts) {
                String formattedAmount1 = String.format("%.2f", amount);
                Transaction transaction1 = Transaction.splitPaymentErrorTransaction(
                        timestamp,
                        "Split payment of " + formattedAmount1 + " " + currency,
                        "Account "
                                + guiltyAccount + " has insufficient funds for a split payment.",
                        splitAmount,
                        currency,
                        accountIbans
                );
                account.getOwner().addTransaction(transaction1);
                account.addTransaction(transaction1);
            }
            return;
        }

        if (ok) {
            for (Account account : accounts) {
                double convertedAmount =
                        currencyConverter.convert(splitAmount, currency, account.getCurrency());
                account.setBalance(account.getBalance() - convertedAmount);

                String formattedAmount = String.format("%.2f", amount);
                Transaction transaction = Transaction.splitPaymentTransaction(
                        timestamp,
                        "Split payment of " + formattedAmount + " " + currency,
                        splitAmount,
                        currency,
                        accountIbans
                );
                account.getOwner().addTransaction(transaction);
                account.addTransaction(transaction);
            }
        }

    }

}
