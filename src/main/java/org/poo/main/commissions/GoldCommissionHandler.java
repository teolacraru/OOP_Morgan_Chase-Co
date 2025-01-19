package org.poo.main.commissions;

import org.poo.main.CurrencyConverter;

/**
 * Handles commission calculation for "gold" plan accounts.
 * No commission is applied for "gold" accounts.
 */
public final class GoldCommissionHandler extends CommissionHandler {

    /**
     * Calculates the commission for a transaction.
     * Returns 0.0 for "gold" plans or delegates to the next handler.
     *
     * @param planType          the account's plan type.
     * @param amount            the transaction amount.
     * @param currency          the transaction currency.
     * @param currencyConverter the currency conversion utility.
     * @return the calculated commission or 0.0 for "gold" plans.
     * @throws IllegalArgumentException if the plan type is unknown.
     */
    @Override
    public double handleCommission(final String planType,
                                   final double amount,
                                   final String currency,
                                   final CurrencyConverter currencyConverter) {
        if (planType.equalsIgnoreCase("gold")) {
            return 0.0;
        }
        if (next != null) {
            return next.handleCommission(planType, amount, currency, currencyConverter);
        }
        throw new IllegalArgumentException("Unknown plan type: " + planType);
    }
}
