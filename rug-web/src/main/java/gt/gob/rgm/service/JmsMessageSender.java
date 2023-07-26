package gt.gob.rgm.service;

import org.springframework.jms.core.JmsTemplate;

public class JmsMessageSender implements JmsMessageSenderService {
	private JmsTemplate jmsTemplate;
	private String jndiDestination;
	
	public JmsMessageSender() {
	}
	
	public void sendMessage(String message) {
		System.out.println("Enviando mensaje JMS[Spring]: " + message);
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
