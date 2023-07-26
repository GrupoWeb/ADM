package gt.gob.rgm.service;

import org.springframework.jms.core.JmsTemplate;

public class JmsSolrMessageSender implements JmsSolrMessageSenderService {
	private JmsTemplate jmsTemplate;
	private String jndiDestination;
	
	public JmsSolrMessageSender() {
	}
	
	public void sendMessage(String message) {
		jmsTemplate.send(jndiDestination, s -> s.createTextMessage(message));
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public String getJndiDestination() {
		return jndiDestination;
	}

	public void setJndiDestination(String jndiDestination) {
		this.jndiDestination = jndiDestination;
	}
}
