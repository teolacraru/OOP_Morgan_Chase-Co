package org.poo.main.commandsPhase2;

import org.poo.main.accounts.Account;

/**
 * Interface defining the strategy for calculating cashback.
 */
public interface CashbackStrategy {

    /**
     * Calculates the cashback for a given transaction.
     *
     * @param amount the amount of the transaction for which cashback is calculated.
     * @param account the account associated with the transaction.
     * @param merchantCategory the category of the merchant where the transaction occurred.
     * @return the calculated cashback amount.
     */
    double calculateCashback(double amount,
                             Account account,
                             String merchantCategory);
}
