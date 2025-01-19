package org.poo.main.commandsPhase2;

import org.poo.main.accounts.Account;

import java.util.Map;

/**
 * A cashback strategy based on the user's spending thresholds.
 * Different cashback rates are applied based on the account type and total spending.
 */
public final class SpendingThresholdCashbackStrategy implements CashbackStrategy {

    private static final double GOLD_CASHBACK_HIGH = 0.007;
    private static final double GOLD_CASHBACK_MID = 0.0055;
    private static final double STANDARD_CASHBACK_HIGH = 0.0025;
    private static final double STANDARD_CASHBACK_MID = 0.002;
    private static final int HIGH_THRESHOLD = 500;
    private static final int MID_THRESHOLD = 300;
    private static final int LOW_THRESHOLD = 100;

    private final Map<String, Double> spendingThresholds = Map.of(
            "standard", 0.001,
            "student", 0.001,
            "silver", 0.003,
            "gold", 0.005
    );

    /**
     * Calculates cashback based on the transaction amount, account type, and total spending.
     *
     * @param amount the transaction amount (must be positive).
     * @param account the account associated with the transaction.
     * @param merchantCategory the merchant category of the transaction.
     * @return the cashback amount for the transaction.
     * @throws IllegalArgumentException if the account is null.
     */
    @Override
    public double calculateCashback(final double amount,
                                    final Account account,
                                    final String merchantCategory) {
        if (account == null) {
            throw new IllegalArgumentException("Account is null");
        }

        account.addToTotalSpent(amount); // Update total spending
        double totalSpent = account.getTotalSpent();
        double cashbackRate = 0.0;

        if (totalSpent >= HIGH_THRESHOLD) {
            cashbackRate = account.getPlanType().equals("gold")
                    ? GOLD_CASHBACK_HIGH
                    : STANDARD_CASHBACK_HIGH;
        } else if (totalSpent >= MID_THRESHOLD) {
            cashbackRate = account.getPlanType().equals("gold")
                    ? GOLD_CASHBACK_MID
                    : STANDARD_CASHBACK_MID;
        } else if (totalSpent >= LOW_THRESHOLD) {
            cashbackRate = spendingThresholds.getOrDefault(account.getPlanType(), 0.0);
        }

        return cashbackRate;
    }
}
