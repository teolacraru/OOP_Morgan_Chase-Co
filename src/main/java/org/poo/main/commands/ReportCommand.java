package org.poo.main.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;

import java.util.List;

/**
 * Command to generate a report for a specific account.
 */
public class ReportCommand implements Command {
    private final int timestamp;
    private final int startTimestamp;
    private final int endTimestamp;
    private final String accountIBAN;
    private final List<User> users;
    private final ArrayNode output;

    /**
     * Constructor for the ReportCommand.
     *
     * @param timestamp      the current timestamp.
     * @param startTimestamp the start timestamp for the report.
     * @param endTimestamp   the end timestamp for the report.
     * @param accountIBAN    the IBAN of the account.
     * @param users          the list of users.
     * @param output         the JSON output node.
     */
    public ReportCommand(final int timestamp,
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
     * Executes the command to generate a report for the account.
     */
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
        ObjectNode reportNode = output.addObject();
        reportNode.put("command", "report");
        ObjectNode outputNode = reportNode.putObject("output");
        outputNode.put("balance", account.getBalance());
        outputNode.put("currency", account.getCurrency());
        outputNode.put("IBAN", account.getIBAN());

        ArrayNode transactionsArray = outputNode.putArray("transactions");

        for (Transaction transaction : account.getTransactions()) {
            if (transaction.getTimestamp() >= startTimestamp
                    && transaction.getTimestamp() <= endTimestamp) {

                ObjectNode transactionNode = transactionsArray.addObject();
                transactionNode.put("description", transaction.getDescription());
                transactionNode.put("timestamp", transaction.getTimestamp());

                switch (transaction.getTransactionType()) {
                    case "createCard":
                        transactionNode.put("account", transaction.getSenderIBAN());
                        transactionNode.put("card", transaction.getCard());
                        transactionNode.put("cardHolder", transaction.getCardHolder());
                        break;
                    case "payOnline":
                        transactionNode.put("amount", transaction.getAmount());
                        transactionNode.put("commerciant", transaction.getCommerciant());
                        break;
                    case "sendMoney":
                        transactionNode.put("amount",
                                transaction.getAmount() + " " + transaction.getCurrency());
                        transactionNode.put("transferType", transaction.getTransferType());
                        transactionNode.put("senderIBAN", transaction.getSenderIBAN());
                        transactionNode.put("receiverIBAN", transaction.getReceiverIBAN());
                        break;
                    case "splitPayment":
                        transactionNode.put("amount", transaction.getAmount());
                        transactionNode.put("currency", transaction.getCurrency());
                        transactionNode.put("description", transaction.getDescription());
                        if (transaction.getSenderIBAN() != null) {
                            transactionNode.put("error", transaction.getSenderIBAN());

                        }
                        ArrayNode involvedAccountsArray =
                                transactionNode.putArray("involvedAccounts");
                        List<String> involvedAccounts = transaction.getAccounts();
                        for (String account1 : involvedAccounts) {
                            involvedAccountsArray.add(account1);
                        }
                        transactionNode.put("timestamp", transaction.getTimestamp());
                    default:
                        break;
                }
            }
        }
        reportNode.put("timestamp", timestamp);
    }
}
