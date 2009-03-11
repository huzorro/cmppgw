package com.enorbus.sms.gw.cmpp.servlet;

import java.io.IOException;
import java.net.MalformedURLException;

import org.mortbay.jetty.Server;
import org.mortbay.xml.XmlConfiguration;
import org.xml.sax.SAXException;

public class HttpServer {

	private Server server;

	public void start() {
		try {
			server = new Server();
			XmlConfiguration configuration = new XmlConfiguration(Thread
					.currentThread().getContextClassLoader().getResource(
							"jetty.xml"));
			configuration.configure(server);
			server.start();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
