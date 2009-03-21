package com.enorbus.sms.gw.cmpp.console.command;

import com.enorbus.sms.gw.cmpp.console.OutputFormatter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Long Zhi
 * @version $Id: AbstractCommand.java 2249 2009-03-10 07:07:25Z zhi.long $
 */
public abstract class AbstractCommand implements Command {
    private boolean isPrintHelp;

    /**
     * Execute a generic command, which includes parsing the options for the
     * command and running the specific task.
     *
     * @param tokens - command arguments
     * @throws Exception
     */
    public void execute(List<String> tokens) throws Exception {
        // Parse the options specified by "-"
        parseOptions(tokens);

        if (isPrintHelp) {
            // Print the help file of the task
            printHelp();
        } else {
            // Run the specified task
            runTask(tokens);
        }
    }

    /**
     * Parse any option parameters in the command arguments specified by a '-'
     * as the first character of the token.
     *
     * @param tokens - command arguments
     * @throws Exception
     */
    protected void parseOptions(List<String> tokens) throws Exception {
        while (!tokens.isEmpty()) {
            String token = tokens.remove(0);
            if (token.startsWith("-")) {
                // Token is an option
                handleOption(token, tokens);
            } else {
                // Push back to list of tokens
                tokens.add(0, token);
                return;
            }
        }
    }

    /**
     * Handle the general options for each command, which includes -h, -?,
     * --help, -D, --version.
     *
     * @param token - option token to handle
     * @param tokens - succeeding command arguments
     * @throws Exception
     */
    protected void handleOption(String token, List<String> tokens) throws Exception {
        isPrintHelp = false;
        // If token is a help option
        if (token.equals("-h") || token.equals("-?") || token.equals("--help")) {
            isPrintHelp = true;
            tokens.clear();
        } else if (token.startsWith("-D")) {
            // If token is a system property define option
            String key = token.substring(2);
            String value = "";
            int pos = key.indexOf("=");
            if (pos >= 0) {
                value = key.substring(pos + 1);
                key = key.substring(0, pos);
            }
            System.setProperty(key, value);
        } else {
            // Token is unrecognized
            OutputFormatter.printInfo("Unrecognized option: " + token);
            isPrintHelp = true;
        }
    }

    /**
     * Run the specific task.
     *
     * @param tokens - command arguments
     * @throws Exception
     */
    protected abstract void runTask(List<String> tokens) throws Exception;

    /**
     * Print the help messages for the specific task
     */
    protected abstract void printHelp();
}
