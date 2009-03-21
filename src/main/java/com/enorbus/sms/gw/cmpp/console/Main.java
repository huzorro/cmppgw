package com.enorbus.sms.gw.cmpp.console;

import com.enorbus.sms.gw.cmpp.console.command.AbstractCommand;
import com.enorbus.sms.gw.cmpp.console.command.Command;
import com.enorbus.sms.gw.cmpp.console.command.ShutdownCommand;
import com.enorbus.sms.gw.cmpp.console.command.StartCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Long Zhi
 * @version $Id: Main.java 2249 2009-03-10 07:07:25Z zhi.long $
 */
public class Main extends AbstractCommand {

    private boolean interactive;
    private String[] helpFile;

    public Main() {
        this(false);
    }

    public Main(boolean interactive) {
        this.interactive = interactive;
        this.helpFile = new String[] {
            interactive ? "Usage: [task] [task-options] [task data]" : "Usage: Main [task] [task-options] [task data]",
            "",
            "Tasks (default task is start):",
            "    start           - Creates and starts a client using a configuration file, or a broker URI.",
            "    stop            - Stops a running client.",
            "",
            "Task Options (Options specific to each task):",
            "    -h,-?,--help    - Display this help information. To display task specific help, use " + (interactive ? "" : "Main ") + "[task] -h,-?,--help",
            "",
            "Task Data:",
            "    - Information needed by each specific task.",
            ""
        };
    }

    /**
     * Main method to run a command shell client.
     *
     * @param args - command line arguments
     * @return 0 for a successful run, -1 if there are any exception
     */
    public static void main(String[] args) {
        // Convert arguments to list for easier management
        List<String> tokens = new ArrayList<String>(Arrays.asList(args));

        Main main = new Main();
        try {
            main.execute(tokens);
        } catch (Exception e) {
            OutputFormatter.printException(e);
        }
    }

    public boolean isInteractive() {
        return interactive;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    /**
     * Parses for specific command task.
     *
     * @param tokens - command arguments
     * @throws Exception
     */
    protected void runTask(List<String> tokens) throws Exception {

        // Process task token
        if (tokens.size() > 0) {
            Command command=null;
            String taskToken = tokens.remove(0);
            if (taskToken.equals("start")) {
                command = new StartCommand();
            } else if (taskToken.equals("stop")) {
                command = new ShutdownCommand();
            } else if (taskToken.equals("help")) {
                printHelp();
            } else {
                printHelp();
            }

            if( command!=null ) {
                command.execute(tokens);
            }
        } else {
            printHelp();
        }

    }

    /**
     * Print the help messages for the browse command
     */
    protected void printHelp() {
        OutputFormatter.printHelp(helpFile);
    }
}
