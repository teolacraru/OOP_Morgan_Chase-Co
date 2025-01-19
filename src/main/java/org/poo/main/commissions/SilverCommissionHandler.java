package org.poo.main.commissions;

import org.poo.main.CurrencyConverter;

/**
 * Handles commission calculation for "silver" plan accounts.
 * Applies a 0.1% commission for transactions of at least 500 RON.
 */
public final class SilverCommissionHandler extends CommissionHandler {

    private static final double COMMISSION_RATE = 0.001;
    private static final double MINIMUM_AMOUNT_IN_RON = 500.0;

    /**
     * Calculates the commission for a transaction.
     * Returns 0.1% commission for "silver" plans if the transaction amount is at least 500 RON.
     * Delegates to the next handler if the plan type is not "silver".
     *
     * @param planType          the account's plan type.
     * @param amount            the transaction amount.
     * @param currency          the transaction currency.
     * @param currencyConverter the utility for currency conversion.
     * @return the calculated commission or 0.0 if conditions are not met.
     * @throws IllegalArgumentException if the plan type is unknown.
     */
    @Override
    public double handleCommission(final String planType,
                                   final double amount,
                                   final String currency,
                                   final CurrencyConverter currencyConverter) {
        if (planType.equalsIgnoreCase("silver")) {
            double amountInRON = currency.equals("RON")
                    ? amount
                    : currencyConverter.convert(amount, currency, "RON");
            return amountInRON >= MINIMUM_AMOUNT_IN_RON ? amount * COMMISSION_RATE : 0.0;
        }
        if (next != null) {
            return next.handleCommission(planType, amount, currency, currencyConverter);
        }
        throw new IllegalArgumentException("Unknown plan type: " + planType);
    }
}
