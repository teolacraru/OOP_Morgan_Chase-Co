package org.poo.main.commissions;

import org.poo.main.CurrencyConverter;

/**
 * Handles commission calculation for "standard" plan accounts.
 * Applies a 0.2% commission for all transactions.
 */
public final class StandardCommissionHandler extends CommissionHandler {

    private static final double COMMISSION_RATE = 0.002;

    /**
     * Calculates the commission for a transaction.
     * Applies a 0.2% commission for "standard" plans.
     * Delegates to the next handler if the plan type is not "standard".
     *
     * @param planType          the account's plan type.
     * @param amount            the transaction amount.
     * @param currency          the transaction currency.
     * @param currencyConverter the utility for currency conversion.
     * @return the calculated commission or delegates to the next handler.
     * @throws IllegalArgumentException if the plan type is unknown.
     */
    @Override
    public double handleCommission(final String planType,
                                   final double amount,
                                   final String currency,
                                   final CurrencyConverter currencyConverter) {
        if (planType.equalsIgnoreCase("standard")) {
            return amount * COMMISSION_RATE;
        }

        if (next != null) {
            return next.handleCommission(planType, amount, currency, currencyConverter);
        }
        throw new IllegalArgumentException("Unknown plan type: " + planType);
    }
}
