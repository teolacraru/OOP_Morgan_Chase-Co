package org.poo.main.commandsPhase2;

import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;
import org.poo.main.commands.Command;
import org.poo.main.CurrencyConverter;

import java.util.List;

public class UpgradePlanCommand implements Command  {
    private final String newPlanType;
    private final String accountIBAN;
    private final int timestamp;
    private final List<User> users;
    private final CurrencyConverter currencyConverter;


    public UpgradePlanCommand(String newPlanType, String accountIBAN, int timestamp, List<User> users, CurrencyConverter currencyConverter) {
        this.newPlanType = newPlanType;
        this.accountIBAN = accountIBAN;
        this.timestamp = timestamp;
        this.users = users;
        this.currencyConverter = currencyConverter;
    }

    @Override
    public void execute() {
        User user = null;
        Account account = null;

        // Find the user and account
        for (User u : users) {
            for (Account acc : u.getAccounts()) {
                if (acc.getIBAN().equals(accountIBAN)) {
                    user = u;
                    account = acc;
                    break;
                }
            }
        }

        if (user == null || account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        if (user.getPlanType().equals(newPlanType)) {
            throw new IllegalArgumentException("The user already has the " + newPlanType + " plan.");
        }

        List<String> plans = List.of("standard", "student", "silver", "gold");
        int currentPlanIndex = plans.indexOf(user.getPlanType());
        int newPlanIndex = plans.indexOf(newPlanType);
        if (newPlanIndex < currentPlanIndex) {
            throw new IllegalArgumentException("You cannot downgrade your plan.");
        }
        double fee = 0;
        if (user.getPlanType().equals("standard") || user.getPlanType().equals("student")) {
            if (newPlanType.equals("silver")) fee = 100;
            else if (newPlanType.equals("gold")) fee = 350;
        } else if (user.getPlanType().equals("silver") && newPlanType.equals("gold")) {
            fee = 250;
        }
        double convertedFee = currencyConverter.convert(fee, "RON", account.getCurrency());

        if (account.getBalance() < convertedFee) {
            throw new IllegalArgumentException("Insufficient funds");
        }


        account.setBalance(account.getBalance() - convertedFee);
        user.upgradePlanType(newPlanType);
        Transaction transaction3 = Transaction.upgradePlanTransaction(accountIBAN, "Upgrade plan", newPlanType, timestamp);
        account.addTransaction(transaction3);
        account.getOwner().addTransaction(transaction3);
    }

}
