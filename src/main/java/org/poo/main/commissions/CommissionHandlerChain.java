package org.poo.main.commissions;

public class CommissionHandlerChain {
    public static CommissionHandler createChain() {
        CommissionHandler standard = new StandardCommissionHandler();
        CommissionHandler student = new StudentCommissionHandler();
        CommissionHandler silver = new SilverCommissionHandler();
        CommissionHandler gold = new GoldCommissionHandler();

        standard.setNext(student);
        student.setNext(silver);
        silver.setNext(gold);

        return standard;
    }
}
