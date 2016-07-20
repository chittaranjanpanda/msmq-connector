package com.demo.msmqconnector.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.msmqconnector.MsmqContext;

/**
 * @author Chittaranjan
 *
 */
public class MsmqMessageSender implements Processor {
	private Logger _log = LoggerFactory.getLogger(MsmqMessageSender.class);
	private MsmqContext msmqContext;

	public MsmqContext getMsmqContext() {
		return msmqContext;
	}

	public void setMsmqContext(MsmqContext msmqContext) {
		this.msmqContext = msmqContext;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();

		String message = in.getBody(String.class);
		String label = in.getHeader("MSMQ_LABEL") != null ? in.getHeader("MSMQ_LABEL", String.class) : "";
		String correlationID = in.getHeader("MSMQ_CORRELATIONID") != null ? in.getHeader("MSMQ_CORRELATIONID", String.class) : "L:none";

		msmqContext.open();

		if (message != null && !message.trim().isEmpty()) {
			msmqContext.send(message, label, correlationID);
		} else {
			_log.error("Message Body is Null or Empty, Unable to send to Msmq");
		}

		msmqContext.close();
	}
}
