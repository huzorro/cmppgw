package com.enorbus.sms.gw.cmpp.console;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Long Zhi
 * @version $Id: OutputFormatter.java 2249 2009-03-10 07:07:25Z zhi.long $
 */
public class OutputFormatter {

    /**
     * Print help messages
     *
     * @param helpMsgs - help messages to print
     */
    public static void printHelp(String[] helpMsgs) {
        for (int i = 0; i < helpMsgs.length; i++) {
            System.out.println(helpMsgs[i]);
        }
        System.out.println();
    }

    /**
     * Print an information message
     *
     * @param info - information message to print
     */
    public static void printInfo(String info) {
        System.out.println("INFO: " + info);
    }

    /**
     * Print an exception message
     *
     * @param e - exception to print
     */
    public static void printException(Exception e) {
        System.out.println("ERROR: " + e);
        e.printStackTrace(System.out);
    }

    /**
     * Print a generic key value mapping
     *
     * @param map to print
     */
    public static void print(Map map) {
        for (Iterator i = map.keySet().iterator(); i.hasNext();) {
            String key = (String)i.next();
            String val = map.get(key).toString();
            System.out.println(key + " = " + val);
        }
        System.out.println();
    }

    /**
     * Print a generic array of strings
     *
     * @param strings - string array to print
     */
    public static void print(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            System.out.println(strings[i]);
        }
        System.out.println();
    }

    /**
     * Print a collection of objects
     *
     * @param collection - collection to print
     */
    public static void print(Collection collection) {
        for (Iterator i = collection.iterator(); i.hasNext();) {
            System.out.println(i.next().toString());
        }
        System.out.println();
    }

    /**
     * Print a java string
     *
     * @param string - string to print
     */
    public static void print(String string) {
        System.out.println(string);
    }
}
