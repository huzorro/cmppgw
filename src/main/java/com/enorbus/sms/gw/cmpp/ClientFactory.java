package com.enorbus.sms.gw.cmpp;

import com.enorbus.sms.gw.cmpp.support.SpringBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.MalformedURLException;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Long Zhi
 * @version $Id: ClientFactory.java 2262 2009-03-11 08:33:55Z zhi.long $
 */
public class ClientFactory {
    private static final Logger logger = LoggerFactory.getLogger(ClientFactory.class);

    public static ClientService createClient(URI config) throws Exception {
        String uri = config.getSchemeSpecificPart();
        ApplicationContext context = createApplicationContext(uri);

        ClientService client = null;
        try {
            client = (ClientService)context.getBean("client");
        } catch (BeansException e) {
        }

        if (client == null) {
            // lets try find by type
            String[] names = context.getBeanNamesForType(ClientService.class);
            for (String name : names) {
                client = (ClientService) context.getBean(name);
                if (client != null) {
                    break;
                }
            }
        }
        if (client == null) {
            throw new IllegalArgumentException("The configuration has no ClientService instance for resource: " + config);
        }

        if (client instanceof ApplicationContextAware) {
        	((ApplicationContextAware)client).setApplicationContext(context);
        }

        // TODO warning resources from the context may not be closed down!

        return client;
    }

    protected static ApplicationContext createApplicationContext(String uri) throws MalformedURLException {
        logger.debug("Now attempting to figure out the type of resource: {}", uri);
        ApplicationContext ctx = new ClassPathXmlApplicationContext(uri);
//        ApplicationContext ctx = new ClassPathXmlApplicationContext(
//                new String[] {
//                        "beans/applicationContext.xml",
//                        "beans/applicationContext-resources.xml"
//                });
        ((AbstractApplicationContext) ctx).registerShutdownHook();
        SpringBeanUtils.setApplicationContext(ctx);
        return ctx;
    }
}
