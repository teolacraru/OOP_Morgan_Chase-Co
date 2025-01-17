package org.poo.main.commandsPhase2;

import org.poo.main.accounts.Account;

import java.util.Map;

public class SpendingThresholdCashbackStrategy implements CashbackStrategy {
    private final Map<String, Double> spendingThresholds = Map.of(
            "standard", 0.001, // 0.1%
            "student", 0.001, // 0.1%
            "silver", 0.003, // 0.3%
            "gold", 0.005 // 0.5%
    );

    @Override
    public double calculateCashback(double amount, Account account, String merchantCategory) {
        if (account == null) {
            throw new IllegalArgumentException("Account is null");
        }

        account.addToTotalSpent(amount); // Actualizăm totalul cheltuit
        double totalSpent = account.getTotalSpent(); // Obține totalul cheltuit per cont

        double cashbackRate = 0.0;

        if (totalSpent >= 500) {
            cashbackRate = account.getPlanType().equals("gold") ? 0.007 : 0.0025;
        } else if (totalSpent >= 300) {
            cashbackRate = account.getPlanType().equals("gold") ? 0.0055 : 0.002;
        } else if (totalSpent >= 100) {
            cashbackRate = spendingThresholds.getOrDefault(account.getPlanType(), 0.0);
        }


        //System.out.println(amount * cashbackRate);
        return cashbackRate;
    }
}
