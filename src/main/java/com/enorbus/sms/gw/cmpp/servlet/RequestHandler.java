package com.enorbus.sms.gw.cmpp.servlet;

import com.enorbus.sms.gw.cmpp.message.CancelRespMessage;
import com.enorbus.sms.gw.cmpp.message.QueryRespMessage;
import com.enorbus.sms.gw.cmpp.processor.MtProcessorImpl;
import com.enorbus.sms.gw.cmpp.processor.Processor;
import com.enorbus.sms.gw.cmpp.support.SpringBeanUtils;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestHandler extends AbstractHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    @Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, int dispatch) throws IOException,
			ServletException {
		((Request) request).setHandled(true);
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		String msg = request.getParameter("msg");

        Processor processor = (MtProcessorImpl) SpringBeanUtils.getBean("mtProcessor");
        if ("query".equals(msg)) {
            QueryRespMessage respMsg = processor.doQuery(request.getParameter("time"), request.getParameter("querycode"));
			if (respMsg != null)
				response.getWriter().print(respMsg.toString());
		} else if ("cancel".equals(msg)) {
			CancelRespMessage respMsg = processor.doCancel(request.getParameter("msgid"));
			if (respMsg != null)
				response.getWriter().print(respMsg.getSuccessId());
		}
	}
}
