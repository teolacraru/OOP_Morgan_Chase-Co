package org.poo.main.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.Transaction;
import org.poo.main.User;
import org.poo.main.accounts.Account;

import java.util.List;

/**
 * Command for deleting a user's account.
 */
public class DeleteAccountCommand implements Command {
    private final int timestamp;
    private final String email;
    private final String accountIBAN;
    private final ArrayNode output; // JSON node for output
    private final List<User> users;

    /**
     * Constructor for DeleteAccountCommand.
     *
     * @param accountIBAN the IBAN of the account to delete.
     * @param timestamp   the timestamp of the command.
     * @param email       the user's email.
     * @param users       the list of users.
     * @param output      the JSON output node.
     */
    public DeleteAccountCommand(final String accountIBAN, final int timestamp, final String email,
                                final List<User> users, final ArrayNode output) {
        this.accountIBAN = accountIBAN;
        this.timestamp = timestamp;
        this.email = email;
        this.users = users;
        this.output = output;
    }

    /**
     * Executes the account deletion command.
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
        Account account = null;
        for (User u : users) {
            for (Account acc : u.getAccounts()) {
                if (acc.getIBAN().equals(accountIBAN)) {
                    account = acc;
                    break;
                }
            }
        }
        if (account.getBalance() == 0) {
            user.getAccounts().remove(account);
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode commandOutput = mapper.createObjectNode();
            commandOutput.put("command", "deleteAccount");

            ObjectNode outputDetails = mapper.createObjectNode();
            outputDetails.put("success", "Account deleted");
            outputDetails.put("timestamp", timestamp);

            commandOutput.set("output", outputDetails);
            commandOutput.put("timestamp", timestamp);

            output.add(commandOutput);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode commandOutput = mapper.createObjectNode();
            commandOutput.put("command", "deleteAccount");

            ObjectNode outputDetails = mapper.createObjectNode();
            outputDetails.put("error",
                    "Account couldn't be deleted - see org.poo.transactions for details");
            outputDetails.put("timestamp", timestamp);

            commandOutput.set("output", outputDetails);
            commandOutput.put("timestamp", timestamp);

            output.add(commandOutput);

            Transaction transaction = Transaction.deleteAccountTransaction(
                    timestamp, "Account couldn't be deleted - there are funds remaining");
            account.getOwner().addTransaction(transaction);
        }

    }
}
