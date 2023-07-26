package gt.gob.rgm.adm.service;

import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import gt.gob.rgm.adm.util.MailUtils;

@MessageDriven(mappedName="jms/MailTopic")
public class MailReceivedServiceImp implements MessageListener {

    @Override
    public void onMessage(Message message) {
    	try {
			String payload = message.getBody(String.class);
			if(!payload.isEmpty() && payload.startsWith("ENVIAR")) {
				int id = Integer.valueOf(payload.split(":")[1]);
				MailUtils.getMailServiceInstance().sendSingleMail(id);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }
}
