package gt.gob.rgm.adm.service;

import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import gt.gob.rgm.adm.solr.PersonasCollection;

@MessageDriven(mappedName="jms/SolrTopic")
public class SolrReceivedServiceImp implements MessageListener {
	
	@Inject
	PersonasCollection personasCollection;

    @Override
    public void onMessage(Message message) {
    	try {
			String payload = message.getBody(String.class);
			if(!payload.isEmpty() && payload.startsWith("INDEXAR")) {
				System.out.println(payload);
				long id = Long.valueOf(payload.split(":")[1]);
				personasCollection.indexarTramite(id);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }
}
