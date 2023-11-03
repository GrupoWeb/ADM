package gt.gob.rgm.rug.svc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.bind.DatatypeConverter;

import com.sun.xml.ws.developer.SchemaValidation;

import gt.gob.rgm.rug.dao.BitacoraOperacionesRepository;
import gt.gob.rgm.rug.dao.BoletaRepository;
import gt.gob.rgm.rug.dao.ConexionDB;
import gt.gob.rgm.rug.dao.PersonaRepository;
import gt.gob.rgm.rug.model.BitacoraOperaciones;
import gt.gob.rgm.rug.model.Boleta;
import gt.gob.rgm.rug.model.RugPersonasFisicas;
import gt.gob.rgm.rug.model.VCodigoPersonas;
import gt.gob.rgm.rug.ws.ConfirmBoletaRGMRequest;
import gt.gob.rgm.rug.ws.ConfirmBoletaRGMResponse;
import gt.gob.rgm.rug.ws.GetUsuarioRGMRequest;
import gt.gob.rgm.rug.ws.GetUsuarioRGMResponse;
import gt.gob.rgm.rug.ws.PagoServicePortType;
import gt.gob.rgm.rug.ws.Persona;
import gt.gob.rgm.rug.ws.RevertBoletaRGMRequest;
import gt.gob.rgm.rug.ws.RevertBoletaRGMResponse;
import gt.gob.rgm.rug.ws.SetBoletaRGMRequest;
import gt.gob.rgm.rug.ws.SetBoletaRGMResponse;
import gt.gob.rgm.rug.ws.Transaccion;

@WebService(endpointInterface = "gt.gob.rgm.rug.ws.PagoServicePortType", serviceName="Pagos")
@SchemaValidation(handler=MyValidator.class)
public class PagoServiceImpl implements PagoServicePortType{
	
	private static final String ORIGINAL = "ÁáÉéÍíÓóÚúÑñÜü";
	private static final String REPLACEMENT = "AaEeIiOoUuNnUu";
	
	@Inject
	BoletaRepository boletaRepository;
	
	@Inject
	PersonaRepository personaRepository;
	
	@Inject
	BitacoraOperacionesRepository bitacoraRepository;

	public GetUsuarioRGMResponse getUsuarioRGM(
			GetUsuarioRGMRequest getUsuarioRGMRequest) {
		final GetUsuarioRGMResponse response = new GetUsuarioRGMResponse();
		
		Persona persona = new Persona();
		VCodigoPersonas rugPersona = new VCodigoPersonas();
		
		rugPersona = personaRepository.findByCodigoRegistro(getUsuarioRGMRequest.getPCodigoPersona());
		
		if(rugPersona == null) {		
			response.setPersona(persona);
			response.setEstado(2);
			response.setDescripcion("NO SE ENCONTRO UNA PERSONA CON ESTE CODIGO.");
		} else {
			RugPersonasFisicas rugPersonaF = new RugPersonasFisicas();
			rugPersonaF = personaRepository.findById(rugPersona.getIdPersona());
			
			if(rugPersonaF == null) {
				response.setPersona(persona);
				response.setEstado(2);
				response.setDescripcion("NO SE ENCONTRO UNA PERSONA CON ESTE CODIGO.");
			} else {
				persona.setNit(rugPersona.getRfc());
				persona.setNombre(stripAccents(rugPersonaF.getNombrePersona()));
				System.out.println("Persona Juridica:" +rugPersona.getPerJuridica());
				if(getUsuarioRGMRequest.getPCodigoPersona().endsWith("I")) {
					persona.setIdPersona(rugPersona.getCurpDoc());
				} else {
					persona.setIdPersona(rugPersona.getRfc());
				}
				
				response.setPersona(persona);
				response.setEstado(0);
				response.setDescripcion("Exito");
			}
		}
		
		BitacoraOperaciones bitacora = new BitacoraOperaciones();
		bitacora.setAgencia(0L);
		bitacora.setOperacion("getUsuarioRGM");
		bitacora.setFecha(Timestamp.valueOf(LocalDateTime.now()));
		bitacora.setDetalle(getUsuarioRGMRequest.getPCodigoPersona()
				+ "|" 
				+ response.getEstado()
				+ ":"
				+ response.getDescripcion());
		bitacoraRepository.save(bitacora);
		
		return response;
	}

	public SetBoletaRGMResponse setBoletaRGM(SetBoletaRGMRequest setBoletaRGMRequest) {
		
		final SetBoletaRGMResponse response = new SetBoletaRGMResponse();
		Float chequespropios = 0.0f;
		Float chequesotros = 0.0f;
		String noCheques = "NO";
			
		
		VCodigoPersonas rugPersona = new VCodigoPersonas();
		
		rugPersona = personaRepository.findByCodigoRegistro(setBoletaRGMRequest.getPCodigoPersona());
		
		if(rugPersona == null) {			
			response.setEstado(2);
			response.setDescripcion("NO SE ENCONTRO UNA PERSONA CON ESTE CODIGO.");
		} else {		
			Boleta boleta = new Boleta();	
				
			boleta = boletaRepository.findByIdActivas(setBoletaRGMRequest.getPNumero(),0);
			
			if(boleta!=null) {
				response.setEstado(3);
				response.setDescripcion("LA BOLETA YA FUE REGISTRADA PREVIAMENTE");
			} else {
				boleta = new Boleta();		
				
				Calendar cal = DatatypeConverter.parseDateTime(setBoletaRGMRequest.getPFechaHora().toString());
				java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis());
				boleta.setFechaHora(timestamp);
				if(setBoletaRGMRequest.getPAgencia()==null || setBoletaRGMRequest.getPAgencia().isEmpty()) {
					boleta.setAgencia("1");
				} else {
					boleta.setAgencia(setBoletaRGMRequest.getPAgencia());
				}
				boleta.setIdTransaccion(new Long(setBoletaRGMRequest.getPIdTransaccion()).toString());
				boleta.setSerie(setBoletaRGMRequest.getPSerie());
				boleta.setNumero(new Long(setBoletaRGMRequest.getPNumero()).toString());				
				boleta.setIdentificador(new Long(rugPersona.getIdPersona()).toString());								
				Float efectivo = setBoletaRGMRequest.getPMonto();
				
				try {
					chequespropios = new Float(setBoletaRGMRequest.getPChequespropios());
				} catch(Exception e) {
					noCheques = "SI";
				}
				try {
					chequesotros = new Float(setBoletaRGMRequest.getPChequesotros());
				} catch(Exception e) {
					noCheques = "SI";
				}
				
				float montoFinal = efectivo;
				
				if(setBoletaRGMRequest.getTipoPago()==null) {					
					if(chequesotros > 0 && chequespropios > 0 ) {
						boleta.setCodigoTramite(4); //Mixto con cheques otros bancos
						montoFinal = montoFinal + setBoletaRGMRequest.getPChequespropios() + setBoletaRGMRequest.getPChequesotros();
					} else if(chequesotros > 0 && efectivo > chequesotros) {
						boleta.setCodigoTramite(4); //Mixto con cheques otros bancos
						montoFinal = montoFinal + setBoletaRGMRequest.getPChequesotros();
					} else if(chequesotros > 0) {
						boleta.setCodigoTramite(3); //cheques otros bancos	
						montoFinal = montoFinal + setBoletaRGMRequest.getPChequesotros();
					} else if(chequespropios > 0) {
						boleta.setCodigoTramite(2); //Cheques propios
						montoFinal = montoFinal + setBoletaRGMRequest.getPChequespropios();
					} else if(efectivo > 0) {
						boleta.setCodigoTramite(1); //Efectivo	
					} else {
						boleta.setCodigoTramite(0); //Default
					}
												
				} else {
					boleta.setCodigoTramite(setBoletaRGMRequest.getTipoPago().intValue());
					if(chequesotros > 0 && chequespropios > 0 ) {
						montoFinal = montoFinal + setBoletaRGMRequest.getPChequespropios() + setBoletaRGMRequest.getPChequesotros();
					} else if(chequesotros > 0 && efectivo > chequesotros) {
						montoFinal = montoFinal + setBoletaRGMRequest.getPChequesotros();
					} else if(chequesotros > 0) {
						montoFinal = montoFinal + setBoletaRGMRequest.getPChequesotros();
					} else if(chequespropios > 0) {
						montoFinal = montoFinal + setBoletaRGMRequest.getPChequespropios();
					} 
				}

				System.out.println("Total " + montoFinal);
												
				boleta.setMonto(new BigDecimal(montoFinal));
				boleta.setMontoOtrosBancos(new BigDecimal(chequesotros));
				boleta.setResolucion(setBoletaRGMRequest.getPReciboContraloria());
				boleta.setUsuario(setBoletaRGMRequest.getPUsuario());
				if(boleta.getCodigoTramite()==3 || boleta.getCodigoTramite()==4) {
					boleta.setUsada(0);
				} else {
					boleta.setUsada(1);
				}
				boleta.setTipoPago("5");
			
				try {
					boletaRepository.save(boleta);
					
					response.setEstado(0);
					response.setDescripcion("SE REGISTRO LA BOLETA EXITOSAMENTE.");

					

					UpdateSaldoBoletas(boleta.getMonto().intValue(),(int) rugPersona.getIdPersona());
				}			
				catch(Exception ex) {
					ex.printStackTrace();
			    	response.setEstado(1);
					response.setDescripcion("ERROR EN EL REGISTRO DE LA BOLETA, FAVOR PROBAR NUEVAMENTE.");					
				}
			}
			
		}
		
		BitacoraOperaciones bitacora = new BitacoraOperaciones();	
		if(setBoletaRGMRequest.getPAgencia()==null || setBoletaRGMRequest.getPAgencia().isEmpty()) {
			bitacora.setAgencia(0L);
		} else {
			if(setBoletaRGMRequest.getPAgencia().equalsIgnoreCase("BI3")) {
				bitacora.setAgencia(Long.valueOf(-3));
			} else {
				bitacora.setAgencia(Long.valueOf(setBoletaRGMRequest.getPAgencia()));
			}
		}		
		bitacora.setOperacion("setBoletaRGM");
		bitacora.setFecha(Timestamp.valueOf(LocalDateTime.now()));
		bitacora.setDetalle(setBoletaRGMRequest.getPAgencia()
				+ ":" + setBoletaRGMRequest.getPCodigoPersona()
				+ ":" + setBoletaRGMRequest.getPIdPersona()
				+ ":" + setBoletaRGMRequest.getPIdTransaccion()
				+ ":" + setBoletaRGMRequest.getPMonto()
				+ ":" + chequespropios
				+ ":" + chequesotros
				+ ":" + setBoletaRGMRequest.getPNumero()
				+ ":" + setBoletaRGMRequest.getPReciboContraloria()
				+ ":" + setBoletaRGMRequest.getPSerie()
				+ ":" + setBoletaRGMRequest.getPUsuario()
				+ ":" + setBoletaRGMRequest.getTipoPago()
				+ ":" + noCheques
				+ "|" 
				+ response.getEstado()
				+ ":"
				+ response.getDescripcion());
		bitacoraRepository.save(bitacora);
		
		return response;
	}
	
	public Integer diffTime(java.sql.Timestamp timestamp2) {
		LocalTime midnight = LocalTime.MIDNIGHT;
		LocalDate today = LocalDate.now(ZoneId.of("America/Guatemala"));
		LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
		LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);
		
		java.sql.Timestamp timestamp1 = java.sql.Timestamp.valueOf(tomorrowMidnight);
		
		// get time difference in seconds
	    long milliseconds = timestamp1.getTime() - timestamp2.getTime();
	    int seconds = (int) milliseconds / 1000;
	 
	    // calculate hours minutes and seconds
	    int hours = seconds / 3600;
	    
	    return hours;
	}
	
	public RevertBoletaRGMResponse revertBoletaRGM(RevertBoletaRGMRequest revertBoletaRGMRequest) {
		final RevertBoletaRGMResponse response = new RevertBoletaRGMResponse();
		
		Boleta boleta = new Boleta();
		boleta = boletaRepository.findByIdActivas(revertBoletaRGMRequest.getPIdTransaccion(),1);
						
		if(boleta != null) {
			
			if(diffTime(boleta.getFechaHora())>=24) {
				response.setEstado(3);
				response.setDescripcion("NO SE PUEDE REVERSAR LA BOLETA PORQUE YA PASO EL HORARIO HABIL DEFINIDIO PARA HACERLO.");
			} else {
				
				if(boletaRepository.findSaldo(boleta.getIdentificador())<boleta.getMonto().doubleValue()) {
					response.setEstado(4);
					response.setDescripcion("NO SE PUEDE REVERSAR LA BOLETA PORQUE YA FUE UTILIZADO EL SALDO.");
				} else {
					boleta.setUsada(-2);
					Calendar cal = DatatypeConverter.parseDateTime(revertBoletaRGMRequest.getPFechaHora().toString());
					java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis());
					boleta.setFechaHora(timestamp);
					boleta.setUsuario(revertBoletaRGMRequest.getPUsuario());
					System.out.println("usuario reversa " + revertBoletaRGMRequest.getPUsuario());
					
					try {
						boletaRepository.update(boleta);
						
						response.setEstado(0);
						response.setDescripcion("SE REVERSO LA BOLETA EXITOSAMENTE.");

						// UpdateSaldoRevertirBoletas(boleta.getMonto().intValue(),(int) rugPersona.getIdPersona());
					} catch(Exception ex) {
						ex.printStackTrace();
						
				    	response.setEstado(1);
						response.setDescripcion("ERROR EN LA REVERSION DE LA BOLETA, FAVOR INTENTE DE NUEVO.");						
					}
				}
			}
		} else {
			response.setEstado(2);
			response.setDescripcion("NO SE ENCONTRO LA BOLETA.");
		}
		
		BitacoraOperaciones bitacora = new BitacoraOperaciones();			
		bitacora.setAgencia(0L);		
		bitacora.setOperacion("revertBoletaRGM");
		bitacora.setFecha(Timestamp.valueOf(LocalDateTime.now()));
		bitacora.setDetalle(revertBoletaRGMRequest.getPUsuario()
				+ ":" + revertBoletaRGMRequest.getPIdTransaccion()
				+ ":" + revertBoletaRGMRequest.getPFechaHora()
				+ "|" 
				+ response.getEstado()
				+ ":"
				+ response.getDescripcion());
		bitacoraRepository.save(bitacora);
		
		return response;
	}

	public ConfirmBoletaRGMResponse confirmBoletaRGM(ConfirmBoletaRGMRequest confirmBoletaRGMRequest) {
		final ConfirmBoletaRGMResponse response = new ConfirmBoletaRGMResponse();
		
		List<Transaccion> transacciones = confirmBoletaRGMRequest.getPTransacciones().getPTransaccion();
		StringBuffer bfOK = new StringBuffer();
		StringBuffer bfError = new StringBuffer();
		
		bfOK.append("SE CONFIRMARON LA BOLETAS EXITOSAMENTE: ");
		bfError.append("NO SE PUDIERON CONFIRMAR LAS BOLETAS: ");
		Integer estado = 0;
		
		for(Transaccion transaccion:transacciones) {
			Boleta boleta = new Boleta();
			boleta.setIdTransaccion(new Long(transaccion.getPIdTransaccion()).toString());
			boleta.setSerie(transaccion.getPSerie());
			boleta.setNumero(new Long(transaccion.getPNumero()).toString());
			BigDecimal vMonto = BigDecimal.valueOf(transaccion.getPMonto()); 
			boleta.setMonto(vMonto.setScale(2, BigDecimal.ROUND_HALF_EVEN));
			
			Boleta boletaFinal = boletaRepository.findByBoleta(boleta); 
			
			if(boletaFinal!=null) {			
				// si es pago de otros cheques lo aprueba
				if(boletaFinal.getCodigoTramite()==3 && boletaFinal.getUsada() == 0) {
					boletaFinal.setUsada(1);
					boletaRepository.update(boletaFinal);
				}
				bfOK.append(" " + boleta.getIdTransaccion());
			} else {
				estado = 1;
				bfError.append(" " + boleta.getIdTransaccion());
			}
		}
		
		if(estado == 0) {
			response.setEstado(estado);
			response.setDescripcion(bfOK.toString());
		} else {
			response.setEstado(estado);
			response.setDescripcion(bfOK.toString() + " y " + bfError.toString());
		}
		
		BitacoraOperaciones bitacora = new BitacoraOperaciones();			
		bitacora.setAgencia(0L);		
		bitacora.setOperacion("confirmBoletaRGM");
		bitacora.setFecha(Timestamp.valueOf(LocalDateTime.now()));
		String detalle = confirmBoletaRGMRequest.getPUsuario()
				+ ":" + confirmBoletaRGMRequest.getPFechaHora()
				+ "[";		
		for(Transaccion tran : confirmBoletaRGMRequest.getPTransacciones().getPTransaccion()) {
			detalle = detalle + "{"
					+ ":" + tran.getPIdTransaccion()
					+ ":" + tran.getPMonto()
					+ ":" + tran.getPNumero()
					+ ":" + tran.getPSerie()
					+ "}";
		}		
		detalle = detalle + "]"
				+ "|" 
				+ response.getEstado()
				+ ":"
				+ response.getDescripcion();
		bitacora.setDetalle(detalle);
		bitacoraRepository.save(bitacora);
		
		return response;
	}
	
	public static String stripAccents(String str) {
	    if (str == null) {
	        return null;
	    }
	    char[] array = str.toCharArray();
	    for (int index = 0; index < array.length; index++) {
	        int pos = ORIGINAL.indexOf(array[index]);
	        if (pos > -1) {
	            array[index] = REPLACEMENT.charAt(pos);
	        }
	    }
	    return new String(array);
	}

	public BoletaRepository getBoletaRepository() {
		return boletaRepository;
	}

	public void setBoletaRepository(BoletaRepository boletaRepository) {
		this.boletaRepository = boletaRepository;
	}

	public boolean UpdateSaldoBoletas(Integer costo, Integer usuario){
		boolean regresa = false;
		Integer flag = 1;
		String sql = "{ call RUG.SP_SUMAR_SALDO( " + " ?, ?, ?) }";
		ConexionDB db = new ConexionDB();
		Connection connection = db.getConnection();
		CallableStatement cs = null;

		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, costo);
			cs.setInt(2, usuario);
			cs.setInt(3, flag);
			cs.execute();
			regresa = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close(connection, null, cs);
		}


		return regresa;
	}

	public boolean UpdateSaldoRevertirBoletas(Integer costo, Integer usuario){
		boolean regresa = false;
		Integer flag = 2;
		String sql = "{ call RUG.SP_SUMAR_SALDO( " + " ?, ?, ?) }";
		ConexionDB db = new ConexionDB();
		Connection connection = db.getConnection();
		CallableStatement cs = null;

		try {
			cs = connection.prepareCall(sql);
			cs.setInt(1, costo);
			cs.setInt(2, usuario);
			cs.setInt(3, flag);
			cs.execute();
			regresa = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close(connection, null, cs);
		}


		return regresa;
	}
}
