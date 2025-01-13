package org.poo.main.commandsPhase2;

import java.util.HashMap;
import java.util.Map;

public class CashbackManager {
    private final Map<String, CashbackStrategy> strategyMap = new HashMap<>();

    public CashbackManager() {
        strategyMap.put("nrOfTransactions", new NrOfTransactionsCashbackStrategy());
        strategyMap.put("spendingThreshold", new SpendingThresholdCashbackStrategy());
    }

    public CashbackStrategy getStrategy(String cashbackType) {
        return strategyMap.getOrDefault(cashbackType, new NoCashbackStrategy());
    }
}

