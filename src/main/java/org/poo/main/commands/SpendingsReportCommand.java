package org.poo.main.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Generates a spendings report for a specified account within a time range.
 */
public class SpendingsReportCommand implements Command {
    private final int timestamp;
    private final int startTimestamp;
    private final int endTimestamp;
    private final String accountIBAN;
    private final List<User> users;
    private final ArrayNode output;

    /**
     * Constructor for SpendingsReportCommand.
     *
     * @param timestamp      the current timestamp.
     * @param startTimestamp the start of the reporting period.
     * @param endTimestamp   the end of the reporting period.
     * @param accountIBAN    the IBAN of the account to generate the report for.
     * @param users          the list of users.
     * @param output         the JSON output node.
     */
    public SpendingsReportCommand(final int timestamp,
                                  final int startTimestamp,
                                  final int endTimestamp,
                                  final String accountIBAN,
                                  final List<User> users,
                                  final ArrayNode output) {
        this.timestamp = timestamp;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.accountIBAN = accountIBAN;
        this.users = users;
        this.output = output;
    }

    /**
     * Executes the command to generate a spendings report.
     */
    @Override
    public void execute() {
        Account account = null;

        for (User user : users) {
            for (Account acc : user.getAccounts()) {
                if (acc.getIBAN().equals(accountIBAN)) {
                    account = acc;
                    break;
                }
            }
        }

        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "spendingsReport");

        ObjectNode reportNode = resultNode.putObject("output");
        if (account.getAccountType().equals("savings")) {
            reportNode.put(
                    "error", "This kind of report is not supported for a saving account");
            resultNode.put("timestamp", timestamp);
            return;
        }
        reportNode.put("balance", account.getBalance());
        reportNode.put("currency", account.getCurrency());
        reportNode.put("IBAN", account.getIBAN());

        ArrayNode transactionsArray = reportNode.putArray("transactions");

        for (Transaction transaction : account.getTransactions()) {
            if (transaction.getTimestamp() >= startTimestamp
                    && transaction.getTimestamp() <= endTimestamp) {
                if (transaction.getTransactionType() != "addAccount"
                        &&
                        transaction.getTransactionType() != "sendMoney"
                        &&
                        transaction.getTransactionType() != "createCard"
                        &&
                        transaction.getTransactionType() != "splitPayment") {
                    transactionsArray.add(transaction.toJson());
                }
            }
        }

        Map<String, Double> commerciantTotals = new HashMap<>();
        for (Transaction transaction : account.getTransactions()) {
            if (transaction.getTimestamp() >= startTimestamp
                    && transaction.getTimestamp() <= endTimestamp) {
                String commerciant = transaction.getCommerciant();
                if (commerciant != null) {
                    double amount = transaction.getAmount();
                    commerciantTotals.put(commerciant,
                            commerciantTotals.getOrDefault(commerciant, 0.0) + amount);
                }
            }
        }

        ArrayNode commerciantsArray = reportNode.putArray("commerciants");
        Map<String, Double> sortedCommerciantTotals = new TreeMap<>(commerciantTotals);

        for (Map.Entry<String, Double> entry : sortedCommerciantTotals.entrySet()) {
            ObjectNode commerciantNode = commerciantsArray.addObject();
            commerciantNode.put("commerciant", entry.getKey());
            double value = entry.getValue();
            commerciantNode.put("total", value);
        }

        resultNode.put("timestamp", timestamp);
    }
}
