package org.poo.main.commandsPhase2;

import org.poo.main.accounts.Account;

import java.util.HashMap;
import java.util.Map;

public class NrOfTransactionsCashbackStrategy implements CashbackStrategy {
    private final Map<String, Integer> transactionCount = new HashMap<>();
    private final Map<String, Double> discountMap = Map.of(
            "Food", 0.02, // 2% pentru 2 tranzacții
            "Clothes", 0.05, // 5% pentru 5 tranzacții
            "Tech", 0.1 // 10% pentru 10 tranzacții
    );

    @Override
    public double calculateCashback(double amount, Account account, String merchantCategory) {
        transactionCount.put(merchantCategory, transactionCount.getOrDefault(merchantCategory, 0) + 1);

        int count = transactionCount.get(merchantCategory);
        double cashback = 0.0;

        if (count == 2 && "Food".equals(merchantCategory)) {
            cashback = amount * 0.02;
            transactionCount.put(merchantCategory, 0); // Resetăm contorul pentru Food
        } else if (count == 5 && "Clothes".equals(merchantCategory)) {
            cashback = amount * 0.05;
            transactionCount.put(merchantCategory, 0); // Resetăm contorul pentru Clothes
        } else if (count == 10 && "Tech".equals(merchantCategory)) {
            cashback = amount * 0.1;
            transactionCount.put(merchantCategory, 0); // Resetăm contorul pentru Tech
        }

        return cashback;
    }

}