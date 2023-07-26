package gt.gob.rgm.adm.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import gt.gob.rgm.adm.dao.RugBoletaPdfRepository;
import gt.gob.rgm.adm.dao.RugGarantiasRepository;
import gt.gob.rgm.adm.dao.TramitesRepository;
import gt.gob.rgm.adm.model.RugBoletaPdf;
import gt.gob.rgm.adm.model.RugGarantias;
import gt.gob.rgm.adm.model.RugRelTramGaran;
import gt.gob.rgm.adm.model.Tramites;
import gt.gob.rgm.adm.util.Constants;
import gt.gob.rgm.adm.util.PageXofY;
import gt.gob.rgm.adm.util.Random;


@Stateless
public class RugBoletaPdfServiceImp implements RugBoletaPdfService {
	
	@Inject
	private RugBoletaPdfRepository boletaDao;
	
	@Inject
	TramitesRepository tramitesDao;
	
	@Inject
	RugGarantiasRepository garantiasDao;
	
	@Inject
	RugRelTramGaranService relTramGaranService;

	@Override
	public List<RugBoletaPdf> getBoletasByTramite(Long idTramite) {
		return boletaDao.findByTramite(idTramite);
	}
	
	@Override
	public RugBoletaPdf getBoleta(Long id) {
		return boletaDao.findById(id);
	}

	@Override
	public void saveBoleta(Long idTramite, byte[] archivo) {
		// obtener tramite a partir de garantia
		Tramites tramite = tramitesDao.findById(idTramite);
		
		// guardar boleta
		RugBoletaPdf boleta = new RugBoletaPdf();
		boleta.setIdTramite(new BigDecimal(tramite.getIdTramite()));
		boleta.setArchivo(archivo);
		String key = Long.valueOf(tramite.getIdTramite() + Random.generateRandom(Constants.RANDOM_LIMIT)).toString();
		boleta.setPdfKey(key);
		boleta.setIdPersona(new BigDecimal(tramite.getIdPersona()));
		boleta.setFechaReg(Timestamp.valueOf(LocalDateTime.now()));
		boleta.setStatusReg(Constants.ESTADO_ACTIVO);
		boletaDao.save(boleta);
	}
}
