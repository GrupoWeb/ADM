package mx.gob.se.rug.util.pdf;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import gt.gob.rgm.captcha.utils.Random;
import mx.gob.se.rug.constants.Constants;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.events.PdfDocumentEvent;

public class PageXofY implements IEventHandler {
	
	private BarcodeQRCode qrCode;
    private Image qrCodeAsImage;
	    
    public PageXofY(String pKey) {
    	//Create the QR-code
        qrCode = new BarcodeQRCode("placeholder");
        Constants constants = new Constants();
		String server = constants.getParamValue(Constants.URL_SERVER);
		qrCode.setCode(server + "/Rug/rs/home/validar?token=" + pKey);
		
    }
    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        
        qrCodeAsImage = new Image(qrCode.createFormXObject(pdf));
        qrCodeAsImage.scaleAbsolute(75, 75);
		qrCodeAsImage.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		
		PdfPage page = docEvent.getPage();
        PdfCanvas aboveCanvas = new PdfCanvas(page.newContentStreamAfter(),
                page.getResources(), pdf);
        Rectangle area = page.getPageSize();
        new Canvas(aboveCanvas, pdf, area)
                .add(qrCodeAsImage);        		
    }    
}
