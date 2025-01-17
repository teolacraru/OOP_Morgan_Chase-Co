package org.poo.main.commissions;

import org.poo.main.CurrencyConverter;

public class GoldCommissionHandler extends CommissionHandler {
    @Override
    public double handleCommission(String planType, double amount, String currency, CurrencyConverter currencyConverter) {
        if (planType.equalsIgnoreCase("gold")) {
            return 0.0; // Fără comision
        }
        if (next != null) {
            return next.handleCommission(planType, amount, currency, currencyConverter);
        }
        throw new IllegalArgumentException("Unknown plan type: " + planType);
    }
}
