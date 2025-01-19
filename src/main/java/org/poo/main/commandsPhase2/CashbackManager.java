package org.poo.main.commandsPhase2;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the cashback strategies available for different cashback types.
 * It acts as a factory for retrieving the appropriate cashback strategy
 * based on the provided type.
 */
public class CashbackManager {
    private final Map<String, CashbackStrategy> strategyMap = new HashMap<>();

    public CashbackManager() {
        strategyMap.put("nrOfTransactions", new NrOfTransactionsCashbackStrategy());
        strategyMap.put("spendingThreshold", new SpendingThresholdCashbackStrategy());
    }

    /**
     * Retrieves the cashback strategy corresponding to the provided type.
     * If the provided type does not match any strategy, a {@link NoCashbackStrategy}
     * is returned by default.
     *
     * @param cashbackType the type of cashback strategy to retrieve (e.g., "nrOfTransactions").
     * @return the cashback strategy associated with the provided type,
     * or a default no-op strategy.
     */
    public CashbackStrategy getStrategy(final String cashbackType) {
        return strategyMap.getOrDefault(cashbackType, new NoCashbackStrategy());
    }
}

