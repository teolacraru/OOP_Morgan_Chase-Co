package org.poo.main.commandsPhase2;

import org.poo.main.accounts.Account;

public interface CashbackStrategy {
    double calculateCashback(double amount, Account account, String merchantCategory);
}

