package org.poo.main.commandsPhase2;

import org.poo.main.CurrencyConverter;
import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.accounts.SavingsAccount;
import org.poo.main.commands.Command;

import java.util.List;

public class WithdrawSavingsCommand implements Command {
    private final String accountIBAN;
    private final double amount;
    private final String currency;
    private final int timestamp;
    private final List<User> users;
    private final CurrencyConverter currencyConverter;

    /**
     * Constructor for WithdrawSavingsCommand.
     *
     * @param accountIBAN       the IBAN of the savings account.
     * @param amount            the amount to withdraw.
     * @param currency          the currency for the withdrawal.
     * @param timestamp         the timestamp of the transaction.
     * @param users             the list of users.
     * @param currencyConverter the currency converter utility.
     */
    public WithdrawSavingsCommand(final String accountIBAN, final double amount, final String currency,
                                  final int timestamp, final List<User> users,
                                  final CurrencyConverter currencyConverter) {
        this.accountIBAN = accountIBAN;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.users = users;
        this.currencyConverter = currencyConverter;
    }
    public void execute(){
        User user = null;
        SavingsAccount savingsAccount = null;
        Account targetAccount = null;

        for (User u : users) {
            for (Account acc : u.getAccounts()) {
                if (acc.getIBAN().equals(accountIBAN)) {
                    if (acc instanceof SavingsAccount) {
                        savingsAccount = (SavingsAccount) acc;
                        user = u;
                        break;
                    } else {
                        throw new IllegalArgumentException("Account is not of type savings.");
                    }
                }
            }
        }

        if (savingsAccount == null) {
            throw new IllegalArgumentException("Account not found.");
        }

        int age = user.calculateAge();
        if(age < 21) {
            Transaction transaction = Transaction.addAccountTransaction(
                    timestamp,
                    "You don't have the minimum age required.",
                    accountIBAN,
                    currency
            );
            user.addTransaction(transaction);
            savingsAccount.addTransaction(transaction);
            return;
        }
        for (Account acc : user.getAccounts()) {
            if (!acc.equals(savingsAccount) && acc.getCurrency().equals(currency)) {
                targetAccount = acc;
                break;
            }
        }

        if (targetAccount == null) {
            Transaction transaction = Transaction.addAccountTransaction(timestamp, "You do not have a classic account.", null, null);
            user.addTransaction(transaction);
            //savingsAccount.getOwner().addTransaction(transaction);
            return;
        }

        double convertedAmount = currencyConverter.convert(amount, currency, savingsAccount.getCurrency());
        if (savingsAccount.getBalance() < convertedAmount) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        savingsAccount.setBalance(savingsAccount.getBalance() - convertedAmount);
        targetAccount.setBalance(targetAccount.getBalance() + convertedAmount);

        Transaction transaction = Transaction.addAccountTransaction(
                timestamp,
                "Savings withdrawal",
                accountIBAN,
                currency
        );
        user.addTransaction(transaction);
        savingsAccount.addTransaction(transaction);
    }
}
