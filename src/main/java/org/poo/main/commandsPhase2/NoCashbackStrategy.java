package org.poo.main.commandsPhase2;

import org.poo.main.accounts.Account;

public class NoCashbackStrategy implements CashbackStrategy {
    @Override
    public double calculateCashback(double amount, Account account, String merchantCategory) {
        return 0.0; // Niciun cashback
    }
}

