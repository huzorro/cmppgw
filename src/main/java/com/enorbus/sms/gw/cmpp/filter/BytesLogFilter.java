package com.enorbus.sms.gw.cmpp.filter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author shiwang
 * @version $Id: BytesLogFilter.java 2081 2009-02-16 06:37:49Z shishuo.wang $
 *
 */
public class BytesLogFilter extends IoFilterAdapter {
	
	private OutputStream out;
	
	public BytesLogFilter() {
		try {
			out = new FileOutputStream("bytes.log");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {
		IoBuffer buffer = (IoBuffer) message;
		
		if (buffer.hasRemaining()) {
			InputStream in = buffer.asInputStream();
			byte[] b = new byte[buffer.remaining()];
			in.read(b);
			in.close();
			buffer.position(0);
			out.write("RECEIVED".getBytes());
			out.write(b);
		}
		super.messageReceived(nextFilter, session, message);
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		IoBuffer buffer = (IoBuffer) writeRequest.getMessage();
		if (buffer.hasRemaining()) {
			InputStream in = buffer.asInputStream();
			byte[] b = new byte[buffer.remaining()];
			in.read(b);
			in.close();
			out.write("SENT".getBytes());
			out.write(b);
		}
		super.messageSent(nextFilter, session, writeRequest);
	}
	
}
