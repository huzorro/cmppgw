package com.enorbus.sms.gw.cmpp;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Long Zhi
 * @version $Id: Service.java 2249 2009-03-10 07:07:25Z zhi.long $
 */
public interface Service {
    void start() throws Exception;
    void stop() throws Exception;    
}
