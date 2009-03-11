package com.enorbus.sms.gw.cmpp.support;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统配置类
 *
 * @author Long Zhi
 * @version $Id: Config.java 2213 2009-03-05 03:25:10Z zhi.long $
 */
public class Config {
    public static final int DEF_ACTIVE_TEST_INTERVAL = 180;
    public static final int DEF_ACTIVE_TEST_TIMEOUT = 60;
    public static final int DEF_ACTIVE_TEST_MAX_RETRY = 3;
    public static final int DEF_TRANSPORT_TIMEOUT = 60;
    public static final int DEF_TRANPORT_MAX_RETRY = 3;
    public static final int DEF_TRAFFIC_CONTROL = 16;
    public static final int DEF_QUERY_TIMEOUT = 10;
    public static final boolean DEF_MSGID_REVERSE = false;
    public static final boolean SUPPORT_LONG_MSG_PROTOCOL = false;

    /** 默认MT连接数 */
    public static final int DEF_MT_CONNECTION_NUMBER = 1;

    /** 默认MO连接数 */
    public static final int DEF_MO_CONNECTION_NUMBER = 1;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private XMLConfiguration config;

    private Config() {
        try {
            config = new XMLConfiguration("config.xml");
            // Automatic reloading once file changes
            FileChangedReloadingStrategy reloadStrategy = new FileChangedReloadingStrategy();
            // 产品环境中刷新时间应设置得长一些，默认为5000（单位，毫秒）
//            reloadStrategy.setRefreshDelay(60000);
            config.setReloadingStrategy(reloadStrategy);
        }
        catch (ConfigurationException e) {
            logger.error("Exception thrown while loading configuration: ", e);
        }
    }

    static class ConfigHolder {
        static Config instance = new Config();
    }

    public static Config getInstance() {
        return ConfigHolder.instance;
    }

    public String getSpId() {
        return config.getString("spId");
    }

    public String getSharedSecret() {
        return config.getString("sharedSecret");
    }

    public String getIsmgHost() {
        return config.getString("ismgHost");
    }

    public int getIsmgPort() {
        return config.getInt("ismgPort");
    }
    public String getQName() {
        return config.getString("qName");
    }
    public String getGwId() {
        return config.getString("gwId");
    }
   
    public int getActiveTestInterval() {
//        FileChangedReloadingStrategy strategy = (FileChangedReloadingStrategy) config.getReloadingStrategy();
//        System.out.println(strategy.reloadingRequired());
        return config.getInt("activeTestInterval", DEF_ACTIVE_TEST_INTERVAL);
    }

    public int getActiveTestTimeout() {
        return config.getInt("activeTestTimeout", DEF_ACTIVE_TEST_TIMEOUT);
    }

    public int getActiveTestMaxRetry() {
        return config.getInt("activeTestMaxRetry", DEF_ACTIVE_TEST_MAX_RETRY);
    }

    public int getTransportTimeout() {
        return config.getInt("tranportTimeout", DEF_TRANSPORT_TIMEOUT);
    }

    public int getTransportMaxRetry() {
        return config.getInt("transportMaxRetry", DEF_TRANPORT_MAX_RETRY);
    }

    public int getTrafficControl() {
        return config.getInt("trafficControl", DEF_TRAFFIC_CONTROL);
    }
    
    public int getQueryTimeout() {
    	return config.getInt("queryTimeout", DEF_QUERY_TIMEOUT);
    }
    
    public boolean isMsgIdReverse() {
    	return config.getBoolean("msgIdReverse", DEF_MSGID_REVERSE);
    }

    public int getMtConnectionNumber() {
        return config.getInt("mtConnectionNumber", DEF_MT_CONNECTION_NUMBER);
    }

    public int getMoConnectionNumber() {
        return config.getInt("moConnectionNumber", DEF_MO_CONNECTION_NUMBER);        
    }

    public boolean isSupportLongMsgProtocol() {
        return config.getBoolean("supportLongMsgProtocol", SUPPORT_LONG_MSG_PROTOCOL);
    }
}
