package com.enorbus.sms.gw.cmpp.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring BeanFactoryπ§æﬂ¿‡
 *
 * @author Long Zhi
 * @version $Id: SpringBeanUtils.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class SpringBeanUtils {
    private static ApplicationContext ctx;

/*    static {
        ctx = new ClassPathXmlApplicationContext(
                new String[] {
                        "beans/applicationContext.xml",
                        "beans/applicationContext-resources.xml"
                });
    }*/

    public static void setApplicationContext(ApplicationContext context) {
        ctx = context;
    }

    public static Object getBean(String beanName) {
        return ctx.getBean(beanName);
    }
}
