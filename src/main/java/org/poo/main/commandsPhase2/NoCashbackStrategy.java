package org.poo.main.commandsPhase2;

import org.poo.main.accounts.Account;

/**
 * A cashback strategy that provides no cashback.
 * Always returns a cashback amount of 0.0.
 */
public class NoCashbackStrategy implements CashbackStrategy {

    /**
     * Calculates cashback for the transaction.
     * This implementation always returns 0.0.
     *
     * @param amount the transaction amount.
     * @param account the account associated with the transaction.
     * @param merchantCategory the merchant category of the transaction.
     * @return always 0.0 as no cashback is applied.
     */
    @Override
    public double calculateCashback(final double amount,
                                    final Account account,
                                    final String merchantCategory) {
        return 0.0;
    }
}
