package org.poo.main;

import org.poo.main.commandsPhase2.CashbackManager;
import org.poo.main.commandsPhase2.CashbackStrategy;

public class Commerciant {
    private String name;
    private int id;
    private String account;
    private String type;
    private CashbackStrategy cashbackStrategy;
    private String cashbackType; // nrOfTransactions, spendingThreshold


    // Constructor
    public Commerciant(String name, int id, String account, String type, CashbackStrategy cashbackStrategy) {
        this.name = name;
        this.id = id;
        this.account = account;
        this.type = type;
        this.cashbackStrategy = cashbackStrategy;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getType() {
        return type;
    }

    public CashbackStrategy getCashbackStrategy() {
        return cashbackStrategy;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCashbackStrategy(CashbackStrategy cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }
}
