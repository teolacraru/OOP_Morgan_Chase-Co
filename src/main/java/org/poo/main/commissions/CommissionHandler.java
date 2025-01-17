package org.poo.main.commissions;

import org.poo.main.CurrencyConverter;

public abstract class CommissionHandler {
    protected CommissionHandler next;

    public void setNext(CommissionHandler next) {
        this.next = next;
    }

    public abstract double handleCommission(String planType, double amount, String currency, CurrencyConverter currencyConverter);
}


