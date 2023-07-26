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
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;

public class PageRotationEventHandler implements IEventHandler {
	public static final PdfNumber PORTRAIT = new PdfNumber(0);
    public static final PdfNumber LANDSCAPE = new PdfNumber(90);
    public static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);
    public static final PdfNumber SEASCAPE = new PdfNumber(270);

	private PdfNumber rotation = PORTRAIT;

        public void setRotation(PdfNumber orientation) {
            this.rotation = orientation;
        }

        @Override
        public void handleEvent(Event currentEvent) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
            docEvent.getPage().put(PdfName.Rotate, rotation);
        }
}
