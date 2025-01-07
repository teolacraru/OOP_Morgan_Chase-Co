package org.poo.main;

public class ExchangeRate {
    private final String fromCurrency;
    private final String toCurrency;
    private final double rate;

    public ExchangeRate(final String fromCurrency, final String toCurrency, final double rate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getRate() {
        return rate;
    }
}
