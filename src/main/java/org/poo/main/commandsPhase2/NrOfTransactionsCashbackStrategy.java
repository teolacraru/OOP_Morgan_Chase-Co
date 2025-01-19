package org.poo.main.commandsPhase2;

import org.poo.main.accounts.Account;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements a cashback strategy based on the number of transactions
 * in specific merchant categories.
 * Cashback rates:
 * <ul>
 *     <li>Food: 2% cashback after 2 transactions.</li>
 *     <li>Clothes: 5% cashback after 5 transactions.</li>
 *     <li>Tech: 10% cashback after 10 transactions.</li>
 * </ul>
 */
public final class NrOfTransactionsCashbackStrategy implements CashbackStrategy {

    private static final double FOOD_CASHBACK_RATE = 0.02;
    private static final int FOOD_TRANSACTION_THRESHOLD = 2;

    private static final double CLOTHES_CASHBACK_RATE = 0.05;
    private static final int CLOTHES_TRANSACTION_THRESHOLD = 5;

    private static final double TECH_CASHBACK_RATE = 0.1;
    private static final int TECH_TRANSACTION_THRESHOLD = 10;

    private final Map<String, Integer> transactionCount = new HashMap<>();

    /**
     * Calculates the cashback based on the number of transactions
     * in the specified merchant category.
     *
     * @param amount the transaction amount.
     * @param account the account associated with the transaction.
     * @param merchantCategory the category of the merchant.
     * @return the cashback amount if conditions are met, otherwise 0.0.
     */
    @Override
    public double calculateCashback(final double amount,
                                    final Account account,
                                    final String merchantCategory) {
        // Increment transaction count for the merchant category
        transactionCount.put(merchantCategory,
                transactionCount.getOrDefault(merchantCategory, 0) + 1);

        int count = transactionCount.get(merchantCategory);
        double cashback = 0.0;

        // Check conditions for cashback and reset counter if conditions are met
        if ("Food".equals(merchantCategory) && count == FOOD_TRANSACTION_THRESHOLD) {
            cashback = amount * FOOD_CASHBACK_RATE;
            transactionCount.put(merchantCategory, 0);
        } else if ("Clothes".equals(merchantCategory) && count == CLOTHES_TRANSACTION_THRESHOLD) {
            cashback = amount * CLOTHES_CASHBACK_RATE;
            transactionCount.put(merchantCategory, 0);
        } else if ("Tech".equals(merchantCategory) && count == TECH_TRANSACTION_THRESHOLD) {
            cashback = amount * TECH_CASHBACK_RATE;
            transactionCount.put(merchantCategory, 0);
        }

        return cashback;
    }
}
