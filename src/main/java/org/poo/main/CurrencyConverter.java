package org.poo.main;

import java.util.List;

/**
 * Utility class for currency conversion.
 * Converts an amount from one currency to another using a graph of exchange rates.
 * This class is not designed for extension and should be treated as final.
 */
public final class CurrencyConverter {
    private final CurrencyGraph currencyGraph;

    /**
     * Constructs a CurrencyConverter with the specified exchange rates.
     *
     * @param exchangeRates the list of exchange rates to initialize the currency graph
     */
    public CurrencyConverter(final List<ExchangeRate> exchangeRates) {
        this.currencyGraph = new CurrencyGraph(exchangeRates);
    }

    /**
     * Converts an amount from one currency to another using the currency graph.
     *
     * @param amount       the amount to convert
     * @param fromCurrency the source currency
     * @param toCurrency   the target currency
     * @return the converted amount
     * @throws IllegalArgumentException if the conversion rate is not found
     */
    public double convert(final double amount, final String fromCurrency, final String toCurrency) {
        double conversionRate = currencyGraph.getConversionRate(fromCurrency, toCurrency);
        return amount * conversionRate;
    }
}
