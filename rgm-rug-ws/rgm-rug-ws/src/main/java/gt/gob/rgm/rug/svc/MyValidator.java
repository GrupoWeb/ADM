package gt.gob.rgm.rug.svc;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.xml.ws.developer.ValidationErrorHandler;

public class MyValidator extends ValidationErrorHandler{

    public void warning(SAXParseException exception) throws SAXException {
        handleException(exception);
    }

    public void error(SAXParseException exception) throws SAXException {
        handleException(exception);
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        handleException(exception);
    }

    private void handleException(SAXParseException e) throws SAXException {
        e.printStackTrace();
        // Record in database for tracking etc
        throw e;
    }
}
