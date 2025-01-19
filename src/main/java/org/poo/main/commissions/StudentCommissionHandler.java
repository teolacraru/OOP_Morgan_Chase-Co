package org.poo.main.commissions;

import org.poo.main.CurrencyConverter;

/**
 * Handles commission calculation for "student" plan accounts.
 * No commission is applied for "student" accounts.
 */
public final class StudentCommissionHandler extends CommissionHandler {

    /**
     * Calculates the commission for a transaction.
     * Returns 0.0 for "student" plans or delegates to the next handler.
     *
     * @param planType          the account's plan type.
     * @param amount            the transaction amount.
     * @param currency          the transaction currency.
     * @param currencyConverter the utility for currency conversion.
     * @return the calculated commission or 0.0 for "student" plans.
     * @throws IllegalArgumentException if the plan type is unknown.
     */
    @Override
    public double handleCommission(final String planType,
                                   final double amount,
                                   final String currency,
                                   final CurrencyConverter currencyConverter) {
        if (planType.equalsIgnoreCase("student")) {
            return 0.0;
        }
        if (next != null) {
            return next.handleCommission(planType, amount, currency, currencyConverter);
        }
        throw new IllegalArgumentException("Unknown plan type: " + planType);
    }
}
