package org.poo.main.commissions;

import org.poo.main.CurrencyConverter;

public class SilverCommissionHandler extends CommissionHandler {
    @Override
    public double handleCommission(String planType, double amount, String currency, CurrencyConverter currencyConverter) {
        if (planType.equalsIgnoreCase("silver")) {
            double amountInRON = currency.equals("RON") ? amount
                    : currencyConverter.convert(amount, currency, "RON");
            return amountInRON >= 500 ? amount * 0.001 : 0.0;
        }
        if (next != null) {
            return next.handleCommission(planType, amount, currency, currencyConverter);
        }
        throw new IllegalArgumentException("Unknown plan type: " + planType);
    }
}
