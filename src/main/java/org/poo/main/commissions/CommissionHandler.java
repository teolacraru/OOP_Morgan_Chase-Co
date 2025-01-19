package org.poo.main.commissions;

import org.poo.main.CurrencyConverter;

/**
 * Abstract class for handling commissions in a chain-of-responsibility pattern.
 * Each handler processes a specific type of commission and delegates further if necessary.
 */
public abstract class CommissionHandler {

    /** The next handler in the chain of responsibility. */
    protected CommissionHandler next;

    /**
     * Sets the next handler in the chain of responsibility.
     *
     * @param next the next handler to set.
     */
    public void setNext(final CommissionHandler next) {
        this.next = next;
    }

    /**
     * Processes the commission based on the plan type, amount, and currency.
     * If the current handler cannot handle the commission, it delegates to the next handler.
     *
     * @param planType the plan type of the account.
     * @param amount the amount of the transaction.
     * @param currency the currency of the transaction.
     * @param currencyConverter utility for currency conversion.
     * @return the calculated commission as a double.
     */
    public abstract double handleCommission(
            String planType,
            double amount,
            String currency,
            CurrencyConverter currencyConverter
    );
}
