package com.enorbus.sms.gw.cmpp.console.command;

import com.enorbus.sms.gw.cmpp.console.OutputFormatter;
import com.enorbus.sms.gw.cmpp.ClientService;
import com.enorbus.sms.gw.cmpp.ClientFactory;

import java.util.List;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Long Zhi
 * @version $Id: StartCommand.java 2249 2009-03-10 07:07:25Z zhi.long $
 */
public class StartCommand extends AbstractCommand {
    public static final String DEFAULT_CONFIG_URI = "cmppgw.xml";
    
    protected String[] helpFile = new String[] {
        "Task Usage: Main start [start-options] [uri]",
        "Description: Creates and starts a client using a configuration file, or a client URI.",
        "",
        "Start Options:",
        "    -D<name>=<value>      Define a system property.",
        "    -h,-?,--help          Display the start broker help information.",
        "",
        "URI:",
        "",
        "    XBean based client configuration:",
        "",
        "        Example: Main xbean:file:cmppgw.xml",
        "            Loads the xbean configuration file from the current working directory",
        "        Example: Main xbean:cmppgw.xml",
        "            Loads the xbean configuration file from the classpath",
        ""
    };

    protected void runTask(List<String> clientUris) throws Exception {
        try {
            // If no config uri, use default setting
            if (clientUris.isEmpty()) {
                startClient(new URI(DEFAULT_CONFIG_URI));

                // Set configuration data, if available, which in this case
                // would be the config URI
            } else {
                String strConfigURI;

                while (!clientUris.isEmpty()) {
                    strConfigURI = clientUris.remove(0);

                    try {
                        startClient(new URI(strConfigURI));
                    } catch (URISyntaxException e) {
                        OutputFormatter.printException(e);
                        return;
                    }
                }
            }

            // Prevent the main thread from exiting unless it is terminated
            // elsewhere
        } catch (Exception e) {
            OutputFormatter.printException(new RuntimeException("Failed to execute start task. Reason: " + e, e));
            throw new Exception(e);
        }
    }

    /**
     * Create and run a client specified by the given configuration URI
     *
     * @param configURI
     * @throws Exception
     */
    public void startClient(URI configURI) throws Exception {
        System.out.println("Loading client from: " + configURI);
        ClientService client = ClientFactory.createClient(configURI);
        client.start();
    }

    protected void printHelp() {
        OutputFormatter.printHelp(helpFile);
    }
}
