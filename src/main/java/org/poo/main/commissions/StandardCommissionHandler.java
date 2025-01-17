package org.poo.main.commissions;

import org.poo.main.CurrencyConverter;

public class StandardCommissionHandler extends CommissionHandler {
    @Override
    public double handleCommission(String planType, double amount, String currency, CurrencyConverter currencyConverter) {
        if (planType.equalsIgnoreCase("standard")) {
            return amount * 0.002; // 0.2% comision
        }
        if (next != null) {
            return next.handleCommission(planType, amount, currency, currencyConverter);
        }
        throw new IllegalArgumentException("Unknown plan type: " + planType);
    }
}
