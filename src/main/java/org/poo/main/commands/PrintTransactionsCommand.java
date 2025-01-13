package org.poo.main.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.Transaction;
import org.poo.main.User;

import java.util.List;

/**
 * Command to print transactions for a specific user.
 */
public class PrintTransactionsCommand implements Command {
    private final String email;
    private final int timestamp;
    private final List<User> users;
    private final ArrayNode output;

    /**
     * Constructor for PrintTransactionsCommand.
     *
     * @param email     the email of the user.
     * @param timestamp the current timestamp.
     * @param users     the list of users.
     * @param output    the JSON output node.
     */
    public PrintTransactionsCommand(final String email,
                                    final int timestamp,
                                    final List<User> users,
                                    final ArrayNode output) {
        this.email = email;
        this.timestamp = timestamp;
        this.users = users;
        this.output = output;
    }

    /**
     * Executes the command to print transactions for the user.
     */
    @Override
    public void execute() {
        User user = null;

        for (User u : users) {
            if (u.getEmail().equals(email)) {
                user = u;
                break;
            }
        }

        if (user == null) {
            ObjectNode errorNode = output.addObject();
            errorNode.put("command", "printTransactions");
            errorNode.put("timestamp", timestamp);
            errorNode.put("error", "User not found");
            return;
        }

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "printTransactions");
        ArrayNode transactionsArray = resultNode.putArray("output");
        for (Transaction transaction : user.getTransactions()) {
            ObjectNode transactionNode = transactionsArray.addObject();
            renderTransaction(transaction, transactionNode);
        }

        resultNode.put("timestamp", timestamp);
    }

    /**
     * Renders a transaction into the JSON output node.
     *
     * @param transaction     the transaction to render.
     * @param transactionNode the JSON node to populate.
     */
    private void renderTransaction(final Transaction transaction,
                                   final ObjectNode transactionNode) {
        switch (transaction.getTransactionType()) {
            case "sendMoney":
                transactionNode.put("amount",
                        transaction.getAmount() + " " + transaction.getCurrency());
                transactionNode.put("description", transaction.getDescription());
                transactionNode.put("receiverIBAN", transaction.getReceiverIBAN());
                transactionNode.put("senderIBAN", transaction.getSenderIBAN());
                transactionNode.put("timestamp", transaction.getTimestamp());
                transactionNode.put("transferType", transaction.getTransferType());
                break;
            case "createCard":
                transactionNode.put("account", transaction.getSenderIBAN());
                transactionNode.put("card", transaction.getCard());
                transactionNode.put("cardHolder", transaction.getCardHolder());
                transactionNode.put("description", transaction.getDescription());
                transactionNode.put("timestamp", transaction.getTimestamp());
                break;
            case "payOnline":
                double balance = transaction.getAmount();
                transactionNode.put("amount", balance); // Store as integer if no decimals
                transactionNode.put("commerciant", transaction.getCommerciant());
                transactionNode.put("description", transaction.getDescription());
                transactionNode.put("timestamp", transaction.getTimestamp());
                break;
            case "addAccount":
                transactionNode.put("description", transaction.getDescription());
                transactionNode.put("timestamp", transaction.getTimestamp());
                break;
            case "deleteCard":
                transactionNode.put("account", transaction.getSenderIBAN());
                transactionNode.put("card", transaction.getCard());
                transactionNode.put("cardHolder", transaction.getCardHolder());
                transactionNode.put("description", transaction.getDescription());
                transactionNode.put("timestamp", transaction.getTimestamp());
                break;
            case "splitPayment":
                balance = transaction.getAmount();
                if (balance % 1 == 0) {
                    transactionNode.put("amount", (int) balance); // Store as integer if no decimals
                } else {
                    transactionNode.put("amount", Math.round(balance * 100.0) / 100.0); // Store as double with 2 decimals
                }
                transactionNode.put("currency", transaction.getCurrency());
                transactionNode.put("description", transaction.getDescription());
                if (transaction.getSenderIBAN() != null) {
                    transactionNode.put("error", transaction.getSenderIBAN());

                }
                ArrayNode involvedAccountsArray =
                        transactionNode.putArray("involvedAccounts");
                List<String> involvedAccounts = transaction.getAccounts();
                for (String account : involvedAccounts) {
                    involvedAccountsArray.add(account);
                }
                transactionNode.put("timestamp", transaction.getTimestamp());
            case "deleteAccount":
                transactionNode.put("description", transaction.getDescription());
                transactionNode.put("timestamp", transaction.getTimestamp());
            default:
                transactionNode.put("description", transaction.getDescription());
                transactionNode.put("timestamp", transaction.getTimestamp());
        }
    }

}
