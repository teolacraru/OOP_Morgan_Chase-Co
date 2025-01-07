package org.poo.main;

import java.util.List;

public class CurrencyConverter {
    private final CurrencyGraph currencyGraph;

    public CurrencyConverter(final List<ExchangeRate> exchangeRates) {
        this.currencyGraph = new CurrencyGraph(exchangeRates);
    }

    /*
    * This converts an amount to the wanted currency using BFS
    */
    public double convert(final double amount, final String fromCurrency, final String toCurrency) {
        double conversionRate = currencyGraph.getConversionRate(fromCurrency, toCurrency);
        return amount * conversionRate;
    }
}
