package org.poo.main.commands;

import org.poo.main.CurrencyConverter;
import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.splitpayments2.SplitPaymentStatus;

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
    private final String splitPaymentType;
    private final List<Double> amountForUsers;
    private final SplitPaymentStatus status;


    /**
     * Constructor for SplitPaymentCommand.
     *
     * @param timestamp         the timestamp of the command.
     * @param currency          the currency of the payment.
     * @param amount            the amount to be split.
     * @param accountIbans      the list of account IBANs involved.
     * @param users             the list of users in the system.
     * @param currencyConverter the currency converter utility.
     */
    public SplitPaymentCommand(final int timestamp,
                               final double amount,
                               final String currency,
                               final String splitPaymentType, // Adăugat
                               final List<String> accountIbans,
                               final List<Double> amountForUsers,
                               final List<User> users,
                               final CurrencyConverter currencyConverter,
                               final SplitPaymentStatus status) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.currency = currency;
        this.splitPaymentType = splitPaymentType;
        this.accountIbans = accountIbans;
        this.amountForUsers = amountForUsers;
        this.users = users;
        this.currencyConverter = currencyConverter;
        this.status = status;
    }

    /**
     * Executes the split payment command.
     */
    public void execute() {
        for (String iban : accountIbans) {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().equals(iban)) {
                        user.addTransaction(status);
                    }
                }
            }
        }
        if (status.anyRejected()) {
            return;
        }
        if (!status.allAccepted()) {
            return;
        }
        if (status.isFailed()) {
            return;
        }
        List<Account> accounts = new ArrayList<>();
        for (String iban : accountIbans) {
            boolean accountFound = false;
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().equals(iban)) {
                        accounts.add(account);
                        accountFound = true;
                        break;
                    }
                }
                if (accountFound) {
                    break;
                }
            }
            if (!accountFound) {
                throw new IllegalArgumentException("Account not found for IBAN: " + iban);
            }
        }
        List<Double> amountsToPay = new ArrayList<>();
        if (splitPaymentType.equals("equal")) {
            System.out.println(timestamp);
            double equalAmount = amount / accounts.size();
            for (int i = 0; i < accounts.size(); i++) {
                amountsToPay.add(equalAmount);
            }
        } else if (splitPaymentType.equals("custom")) {
            if (amountForUsers.size() != accounts.size()) {
                throw new IllegalArgumentException(
                        "Mismatch between account count and custom amounts.");
            }
            amountsToPay.addAll(amountForUsers);
        } else {
            throw new IllegalArgumentException("Unsupported splitPaymentType: "
                    + splitPaymentType);
        }

        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            double convertedAmount = currencyConverter.convert(amountsToPay.get(i),
                    currency, account.getCurrency());
            if (account.getBalance() < convertedAmount) {
                if (splitPaymentType.equals("custom")) {
                    status.markAsFailed("Insufficient funds for account: "
                            + account.getIBAN());
                    String formattedAmount = String.format("%.2f", amount);
                    Transaction errorTransaction = Transaction.
                            splitPaymentCustomErrorTransaction(
                            timestamp,
                            "Split payment of " + formattedAmount + " " + currency,
                            "Account " + account.getIBAN()
                                    + " has insufficient funds for a split payment.",
                            amount,
                            currency,
                            accountIbans,
                            amountsToPay
                    );
                    for (Account involvedAccount : accounts) {
                        System.out.println(involvedAccount.getIBAN()
                                + " " + timestamp);
                        List<Transaction> allTransactions =
                                involvedAccount.getOwner().getTransactions();
                        int insertIndex = 0;
                        while (insertIndex < allTransactions.size()
                                &&
                                allTransactions.get(insertIndex).getTimestamp()
                                        < errorTransaction.getTimestamp()) {
                            insertIndex++;
                        }
                        allTransactions.add(insertIndex, errorTransaction);
                        involvedAccount.addTransaction(errorTransaction);
                        involvedAccount.getOwner().pollTransaction();
                    }
                } else {
                    status.markAsFailed("Insufficient funds for account: "
                            + account.getIBAN());
                    String formattedAmount = String.format("%.2f", amount);
                    Transaction errorTransaction = Transaction.
                            splitPaymentEqualErrorTransaction(
                            timestamp, "Split payment of " + formattedAmount + " " + currency,
                            "Account " + account.getIBAN()
                                    + " has insufficient funds for a split payment.", amount,
                                    currency, accountIbans
                    );
                    for (Account involvedAccount : accounts) {
                        System.out.println(involvedAccount.getIBAN() + " " + timestamp);
                        List<Transaction> allTransactions =
                                involvedAccount.getOwner().getTransactions();
                        int insertIndex = 0;
                        while (insertIndex < allTransactions.size()
                                && allTransactions.get(insertIndex).getTimestamp()
                                        < errorTransaction.getTimestamp()) {
                            insertIndex++;
                        }
                        allTransactions.add(insertIndex, errorTransaction);
                        involvedAccount.addTransaction(errorTransaction);
                    }
                }
                return;
            }
        }
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            double convertedAmount = currencyConverter.convert(amountsToPay.get(i),
                    currency, account.getCurrency());
            account.setBalance(account.getBalance() - convertedAmount);
            List<Transaction> allTransactions = account.getOwner().getTransactions();
            Transaction transaction;
            if (splitPaymentType.equals("custom")) {
                String formattedAmount = String.format("%.2f", amount);
                transaction = Transaction.splitPaymentCustomTransaction(
                        timestamp, "Split payment of " + formattedAmount
                                + " " + currency, amount, currency, accountIbans, amountsToPay
                );
            } else {
                transaction = Transaction.splitPaymentTransaction(timestamp,
                        "Split payment completed",
                        amountsToPay.get(i), currency, accountIbans
                );
            }
            int insertIndex = 0;
            while (insertIndex < allTransactions.size()
                    && allTransactions.get(insertIndex).getTimestamp()
                            < transaction.getTimestamp()) {
                insertIndex++;
            }
            allTransactions.add(insertIndex, transaction);
            account.addTransaction(transaction);
        }
    }
}

