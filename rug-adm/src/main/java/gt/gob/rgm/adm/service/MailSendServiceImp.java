package gt.gob.rgm.adm.service;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Topic;

@Stateless
public class MailSendServiceImp {
	
	@Inject
    private JMSContext context;
	
    @Resource(lookup = "jms/MailTopic")
    private Topic topic;
    
    public void sendMessage(String message) {
    	System.out.println("Enviando mensaje JMS[JavaEE]: " + message);
    	context.createProducer().send(topic, message);
    }
}
