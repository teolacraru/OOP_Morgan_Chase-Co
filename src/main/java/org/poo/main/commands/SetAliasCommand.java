package org.poo.main.commands;

import org.poo.main.User;

import java.util.List;

/**
 * Command to set an alias for a user's account.
 */
public class SetAliasCommand implements Command {
    private final String email;
    private final String alias;
    private final String accountIBAN;
    private final List<User> users;

    /**
     * Constructor for SetAliasCommand.
     *
     * @param email       the email of the user.
     * @param alias       the alias to be set.
     * @param accountIBAN the IBAN of the account to associate with the alias.
     * @param users       the list of users.
     */
    public SetAliasCommand(final String email,
                           final String alias,
                           final String accountIBAN,
                           final List<User> users) {
        this.email = email;
        this.alias = alias;
        this.accountIBAN = accountIBAN;
        this.users = users;
    }

    /**
     * Executes the command to set an alias for a user's account.
     */
    @Override
    public void execute() {
        User user = null;
        for (User user1 : users) {
            if (user1.getEmail().equals(email)) {
                user = user1;
                break;
            }
        }
        user.addAlias(alias, accountIBAN);
    }
}
