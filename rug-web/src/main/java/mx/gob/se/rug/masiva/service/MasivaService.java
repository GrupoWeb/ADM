package mx.gob.se.rug.masiva.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import mx.gob.se.rug.acreedores.to.AcreedorTO;
import mx.gob.se.rug.exception.InfrastructureException;
import mx.gob.se.rug.exception.NoDataFoundException;
import mx.gob.se.rug.masiva.exception.CargaMasivaException;
import mx.gob.se.rug.masiva.exception.GarantiaRepetidaException;
import mx.gob.se.rug.masiva.exception.NoDataFound;
import mx.gob.se.rug.masiva.exception.XSDException;
import mx.gob.se.rug.masiva.resultado.to.CargaMasivaResultado;
import mx.gob.se.rug.masiva.resultado.to.ResultadoCargaMasiva;
import mx.gob.se.rug.masiva.to.AcreedorAdicional;
import mx.gob.se.rug.masiva.to.ArchivoTO;
import mx.gob.se.rug.masiva.to.CargaMasivaProcess;
import mx.gob.se.rug.masiva.to.ControlError;
import mx.gob.se.rug.masiva.to.Deudor;
import mx.gob.se.rug.masiva.to.Modificacion;
import mx.gob.se.rug.masiva.to.Otorgante;
import mx.gob.se.rug.masiva.to.TipoBienMueble;
import mx.gob.se.rug.masiva.to.Tramite;
import mx.gob.se.rug.masiva.to.string.CargaMasivaPreProcesed;
import mx.gob.se.rug.to.PlSql;
import mx.gob.se.rug.to.UsuarioTO;

public interface MasivaService {

	byte[] convertXMLObjetc(Object obj) throws JAXBException,
			FileNotFoundException, UnsupportedEncodingException;

	String getDigest(byte[] attributes);

	String getSha1FromFile(File in);

	byte[] getBytesFromFile(File file) throws IOException;

	String listToString(List<Integer> lista);

	String getSha1FromFile(byte[] in);

	boolean isAutoridad(Integer id);

	public boolean matchXmltoXsd(File xml, HttpServletRequest request)throws XSDException;

	public String[] stack2string(Exception e);
	
	public ControlError agregaModificacion(Modificacion modificacion,Tramite tramite,UsuarioTO usuarioTO);
	
	
	public List<Integer> getListaTramites();



	public List<Integer> getListaTramitesErrores();


	public ResultadoCargaMasiva getResultado();



	public List<mx.gob.se.rug.masiva.resultado.to.TramiteRes> getInscripcionesErroneas() ;
	
	public void setListaTramites(List<Integer> listaTramites);

	public void setListaTramitesErrores(List<Integer> listaTramitesErrores);

	public void setInscripcionesErroneas(List<mx.gob.se.rug.masiva.resultado.to.TramiteRes> inscripcionesErroneas) ;


	public void setResultado(ResultadoCargaMasiva resultado);
	
	public String getErrorArchivo() ;


	public void setErrorArchivo(String errorArchivo) ;
	
	public PlSql agregaOtorganteInd(Otorgante otorgante, Tramite tramite) ;
	public PlSql agregaOtorgante(List<Otorgante> otorgantes, Tramite tramite) ;
	
	public void isGarantiaRepetida(Integer idGarantia, Set< Integer> idGarantiasInXML) throws GarantiaRepetidaException;

	public void validaGarantia(String idGarantia, Set< Integer> idGarantiasInXML) throws GarantiaRepetidaException, NoDataFoundException, NoDataFound;
	
	public void validaGarantia(Integer idGarantia, Set< Integer> idGarantiasInXML) throws GarantiaRepetidaException, NoDataFoundException, NoDataFound;
	public void changeAcreedorGarantia(Integer idAcreedorNuevo,Integer idAcreedorViejo,Integer idUsuario,Integer idTramiteTemp) throws CargaMasivaException;
	
	public Integer agregaTipoBien(List<TipoBienMueble> bienMuebles,Tramite tramite) throws InfrastructureException, CargaMasivaException;
	
	public CargaMasivaResultado generaFirmaMasiva(CargaMasivaResultado  cargaMasivaResultado,CargaMasivaPreProcesed cargaMasivaPreProcesed,UsuarioTO usuario, ArchivoTO ArchivoViejo, AcreedorTO acreedorTO ) throws InfrastructureException;
	public void generaArchivoResumen(ResultadoCargaMasiva resultado ,UsuarioTO usuarioTO ,ArchivoTO ArchivoViejo,CargaMasivaProcess masivaProcess  ) throws InfrastructureException;	
	
	public void addOtorgantes(List<Otorgante> otorgantes,UsuarioTO usuario, Tramite tramite) throws CargaMasivaException, InfrastructureException;
	public void addDeudores(List<Deudor> deudores,UsuarioTO usuario, Tramite tramite) throws CargaMasivaException, InfrastructureException;
	public void addAcreedorAdicional(List<AcreedorAdicional> acreedoresAdicionales,UsuarioTO usuario, Tramite tramite) throws CargaMasivaException, InfrastructureException;
	public Integer bigIntegerToInteger(BigInteger bigInteger);	
	public void verifyAcreedorOfGarantia(Integer idAcreedor ,Integer idGarantia ) throws InfrastructureException, CargaMasivaException;
}
