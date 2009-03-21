package com.enorbus.sms.gw.cmpp.console.command;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Long Zhi
 * @version $Id: Command.java 2249 2009-03-10 07:07:25Z zhi.long $
 */
public interface Command {

    /**
     * Execute the specified command
     *
     * @param tokens - arguments to the command
     * @throws Exception
     */
    void execute(List<String> tokens) throws Exception;
}
