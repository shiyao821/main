package seedu.address.logic.commands;

import static seedu.address.commons.core.Messages.MESSAGE_CANNOT_REDO_COMMAND;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class RedoCommand extends Command {
    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Redoes the most recent undone command. \n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Redone most recent undone command. ";

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {

        if (!model.canRedoCommand()) {
            throw new CommandException(MESSAGE_CANNOT_REDO_COMMAND);
        }
        model.redoCommand();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return (other == this || other instanceof RedoCommand);
    }
}
