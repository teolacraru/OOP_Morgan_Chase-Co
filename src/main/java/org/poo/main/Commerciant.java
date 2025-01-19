package org.poo.main;

import org.poo.main.commandsPhase2.CashbackStrategy;

/**
 * Represents a merchant with an account, type, and cashback strategy.
 * Provides methods to get and update merchant details.
 */
public final class Commerciant {
    private String name;
    private int id;
    private String account;
    private String type;
    private CashbackStrategy cashbackStrategy;

    /**
     * Constructor for Commerciant.
     *
     * @param name             the merchant's name.
     * @param id               the merchant's unique ID.
     * @param account          the merchant's account identifier.
     * @param type             the merchant's type (e.g., "small business").
     * @param cashbackStrategy the cashback strategy associated with the merchant.
     */
    public Commerciant(final String name, final int id, final String account, final String type,
                       final CashbackStrategy cashbackStrategy) {
        this.name = name;
        this.id = id;
        this.account = account;
        this.type = type;
        this.cashbackStrategy = cashbackStrategy;
    }

    /**
     * Gets the merchant's name.
     *
     * @return the name of the merchant.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the merchant's unique ID.
     *
     * @return the ID of the merchant.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the merchant's unique ID.
     *
     * @param id the new ID to set.
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Gets the merchant's account identifier.
     *
     * @return the account identifier of the merchant.
     */
    public String getAccount() {
        return account;
    }

    /**
     * Sets the merchant's account identifier.
     *
     * @param account the new account identifier to set.
     */
    public void setAccount(final String account) {
        this.account = account;
    }

    /**
     * Gets the merchant's type.
     *
     * @return the type of the merchant.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the merchant's type.
     *
     * @param type the new type to set.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Gets the cashback strategy associated with the merchant.
     *
     * @return the cashback strategy.
     */
    public CashbackStrategy getCashbackStrategy() {
        return cashbackStrategy;
    }

    /**
     * Sets the cashback strategy for the merchant.
     *
     * @param cashbackStrategy the new cashback strategy to set.
     */
    public void setCashbackStrategy(final CashbackStrategy cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }
}
