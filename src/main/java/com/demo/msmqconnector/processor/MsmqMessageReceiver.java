package com.demo.msmqconnector.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.demo.msmqconnector.MsmqContext;

/**
 * @author Chittaranjan
 *
 */
public class MsmqMessageReceiver implements Processor {
	private MsmqContext msmqContext;

	public MsmqContext getMsmqContext() {
		return msmqContext;
	}

	public void setMsmqContext(MsmqContext msmqContext) {
		this.msmqContext = msmqContext;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		msmqContext.open();
		String msg = msmqContext.receive();
		msmqContext.close();

		exchange.getOut().setBody(msg);
	}
}
