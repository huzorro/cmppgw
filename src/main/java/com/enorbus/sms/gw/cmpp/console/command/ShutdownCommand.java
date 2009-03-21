package com.enorbus.sms.gw.cmpp.console.command;

import com.enorbus.sms.gw.cmpp.Service;
import com.enorbus.sms.gw.cmpp.console.OutputFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Long Zhi
 * @version $Id: ShutdownCommand.java 2311 2009-03-20 06:38:57Z zhi.long $
 */
public class ShutdownCommand extends AbstractJmxCommand {

    private boolean isStopAllClients;

    protected String[] helpFile = new String[] {
        "Task Usage: Main stop [stop-options] [client-name1] [client-name2] ...",
        "Description: Stops a running broker.",
        "",
        "Stop Options:",
        "    --all                      Stop all clints.",
        "    -h,-?,--help               Display the stop client help information.",
        "",
        "Client Names:",
        "    Name of the clients that will be stopped.",
        "    If omitted, it is assumed that there is only one client running, and it will be stopped.",
        "    Use -all to stop all running clients.",
        ""
    };

    protected void runTask(List<String> tokens) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans/applicationContext-console.xml");
        Service client = (Service) ctx.getBean("proxy");
        client.stop();
    }

    /**
     * Handle the --all option.
     *
     * @param token - option token to handle
     * @param tokens - succeeding command arguments
     * @throws Exception
     */
    protected void handleOption(String token, List<String> tokens) throws Exception {
        // Try to handle the options first
        if (token.equals("--all")) {
            isStopAllClients = true;
        } else {
            // Let the super class handle the option
            super.handleOption(token, tokens);
        }
    }

    protected void printHelp() {
        OutputFormatter.printHelp(helpFile);
    }
}
