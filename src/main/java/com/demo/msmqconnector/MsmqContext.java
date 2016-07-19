package com.demo.msmqconnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;
import ionic.Msmq.Queue;

/**
 * @author Chittaranjan
 *
 */
public class MsmqContext {
	private String host;
	private String qname;
	private Queue queue = null;
	private Logger _log = LoggerFactory.getLogger(MsmqContext.class);

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getQname() {
		return qname;
	}

	public void setQname(String qname) {
		this.qname = qname;
	}

	/**
	 * Opening a queue for operation
	 */
	public void open() {
		try {
			if (queue != null) {
				queue.close();
				queue = null;
			}
			String fullname = getQueueFullName(this.host, this.qname);
			queue = new Queue(fullname);
		} catch (MessageQueueException exc) {
			_log.error("Queue open failure: " + exc);
		}
	}

	/**
	 * Receive message form queue
	 * 
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	public String receive() throws java.io.UnsupportedEncodingException {
		String msgStr = "";
		try {
			checkOpen();
			Message msg = queue.receive(10);
			msgStr = msg.getBodyAsString();
			_log.info("Message Recieved from Queue " + queue.getName());
		} catch (MessageQueueException exc) {
			_log.info("No Message recieved : " + exc);
		}

		return msgStr;
	}

	/**
	 * Send message to queue
	 * 
	 * @param body
	 * @param label
	 * @param correlationID
	 * @throws java.io.UnsupportedEncodingException
	 */
	public void send(String body, String label, String correlationID) throws java.io.UnsupportedEncodingException {
		try {
			checkOpen();
			Message msg = new Message(body, label, correlationID);
			queue.send(msg);
			_log.info("Message sent to " + queue.getName());
		} catch (MessageQueueException ex) {
			_log.error("send failure: " + ex);
		}
	}

	/**
	 * Close queue after operation
	 */
	public void close() {
		if (queue != null) {
			try {
				queue.close();
				queue = null;
			} catch (MessageQueueException e) {
				_log.error("Can't Close the Queue: " + e);
			}
		}
	}

	/**
	 * Getting the proper queue name based on configuration
	 * 
	 * @param hostname
	 * @param queueShortName
	 * @return
	 */
	private String getQueueFullName(String hostname, String queueShortName) {
		String h = hostname;
		String sys = "OS";
		if ((h == null) || h.equals(""))
			h = ".";
		else
			sys = "OS";

		return "DIRECT=" + sys + ":" + h + "\\private$\\" + queueShortName;
	}

	/**
	 * Checking the queue is open
	 * 
	 * @throws MessageQueueException
	 */
	private void checkOpen() throws MessageQueueException {
		if (queue == null)
			throw new MessageQueueException(queue.getName() + " Queue is not open for operation !\n", -1);
	}

	@Override
	protected void finalize() throws Throwable {
		if (queue != null)
			queue.close();
		super.finalize();
	}
}
