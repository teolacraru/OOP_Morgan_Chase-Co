package org.poo.main;

public class Commerciant {
    private final String name;
    private double amount;

    public Commerciant(final String name, final double amount) {
        this.name = name;
        this.amount = amount;
    }

    /*
    * Getter for name.
     */
    public String getName() {
        return name;
    }

    /*
    * Setter for amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
