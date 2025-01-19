package org.poo.main;

import java.util.*;

/**
 * Represents a graph of currency exchange rates.
 */
public class CurrencyGraph {

    private final Map<String, Map<String, Double>> graph = new HashMap<>();

    /**
     * Constructs a CurrencyGraph using a list of exchange rates.
     *
     * @param exchangeRates the list of exchange rates to initialize the graph
     */
    public CurrencyGraph(final List<ExchangeRate> exchangeRates) {
        for (final ExchangeRate rate : exchangeRates) {
            addExchangeRate(rate.getFromCurrency(), rate.getToCurrency(), rate.getRate());
        }
    }

    /**
     * Adds a conversion rate between two currencies.
     *
     * @param from the source currency
     * @param to   the target currency
     * @param rate the conversion rate from source to target currency
     */
    public void addExchangeRate(final String from, final String to, final double rate) {
        graph.putIfAbsent(from, new HashMap<>());
        graph.putIfAbsent(to, new HashMap<>());

        graph.get(from).put(to, rate);
        graph.get(to).put(from, 1.0 / rate);
    }

    /**
     * Retrieves the conversion rate from one currency to another.
     *
     * @param fromCurrency the source currency
     * @param toCurrency   the target currency
     * @return the conversion rate from source to target currency
     * @throws IllegalArgumentException if no conversion path is found
     */
    public double getConversionRate(final String fromCurrency, final String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return 1.0;
        }
        final Queue<String> queue = new LinkedList<>();
        final Map<String, Double> rates = new HashMap<>();
        final Set<String> visited = new HashSet<>();

        queue.add(fromCurrency);
        rates.put(fromCurrency, 1.0);
        visited.add(fromCurrency);

        while (!queue.isEmpty()) {
            final String current = queue.poll();
            final double currentRate = rates.get(current);

            for (final Map.Entry<String, Double> neighbor : graph.getOrDefault(
                    current, Collections.emptyMap()).entrySet()) {
                final String neighborCurrency = neighbor.getKey();
                final double edgeRate = neighbor.getValue();

                if (!visited.contains(neighborCurrency)) {
                    rates.put(neighborCurrency, currentRate * edgeRate);
                    visited.add(neighborCurrency);
                    queue.add(neighborCurrency);

                    if (neighborCurrency.equals(toCurrency)) {
                        return rates.get(neighborCurrency);
                    }
                }
            }
        }
        throw new IllegalArgumentException("No conversion path found from "
                + fromCurrency + " to " + toCurrency);
    }
}
