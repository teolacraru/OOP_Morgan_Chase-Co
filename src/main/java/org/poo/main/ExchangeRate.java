package org.poo.main;

/**
 * Represents an exchange rate between two currencies.
 * Includes the source currency, target currency, and the exchange rate.
 */
public final class ExchangeRate {

    private final String fromCurrency;
    private final String toCurrency;
    private final double rate;

    /**
     * Constructs an ExchangeRate object.
     *
     * @param fromCurrency the currency being converted from.
     * @param toCurrency   the currency being converted to.
     * @param rate         the exchange rate value.
     */
    public ExchangeRate(final String fromCurrency, final String toCurrency, final double rate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
    }

    /**
     * Gets the source currency.
     *
     * @return the currency being converted from.
     */
    public String getFromCurrency() {
        return fromCurrency;
    }

    /**
     * Gets the target currency.
     *
     * @return the currency being converted to.
     */
    public String getToCurrency() {
        return toCurrency;
    }

    /**
     * Gets the exchange rate value.
     *
     * @return the exchange rate as a double.
     */
    public double getRate() {
        return rate;
    }
}
