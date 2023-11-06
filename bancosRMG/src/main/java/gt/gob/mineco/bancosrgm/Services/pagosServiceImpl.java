package gt.gob.mineco.bancosrgm.Services;

import gt.gob.mineco.bancosrgm.dao.BitacoraOperacionesRepository;
import gt.gob.mineco.bancosrgm.dao.BoletaRepository;
import gt.gob.mineco.bancosrgm.dao.PersonaRepository;
import gt.gob.mineco.bancosrgm.dao.VCodigoPersonasRepository;
import gt.gob.mineco.bancosrgm.models.BitacoraOperaciones;
import gt.gob.mineco.bancosrgm.models.Boleta;
import gt.gob.mineco.bancosrgm.models.RugPersonasFisicas;
import gt.gob.mineco.bancosrgm.models.VCodigoPersonas;
import gt.gob.mineco.bancosrgm.ws.*;
import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

@Service
@SchemaValidation(type = SchemaValidation.SchemaValidationType.REQUEST)
public class pagosServiceImpl implements PagoServicePortType {


    private static final String ORIGINAL = "ÁáÉéÍíÓóÚúÑñÜü";
    private static final String REPLACEMENT = "AaEeIiOoUuNnUu";

    @Inject
    PersonaRepository personaRepository;

    @Inject
    VCodigoPersonasRepository vCodigoPersonasRepository;

    @Inject
    BitacoraOperacionesRepository bitacoraRepository;

    @Inject
    BoletaRepository boletaRepository;


    @Override
    public RevertBoletaRGMResponse revertBoletaRGM(RevertBoletaRGMRequest revertBoletaRGMRequest) {
        final RevertBoletaRGMResponse response = new RevertBoletaRGMResponse();

        Boleta boleta  = boletaRepository.findByIdActivasTransaccion(revertBoletaRGMRequest.getPIdTransaccion());


        if(boleta != null) {

            if(diffTime(boleta.getFechaHora())>=24) {
                response.setEstado(3);
                response.setDescripcion("NO SE PUEDE REVERSAR LA BOLETA PORQUE YA PASO EL HORARIO HABIL DEFINIDIO PARA HACERLO.");
            } else {
                if(boletaRepository.findSaldo(boleta.getIdentificador()).doubleValue() < boleta.getMonto().doubleValue()) {
                    response.setEstado(4);
                    response.setDescripcion("NO SE PUEDE REVERSAR LA BOLETA PORQUE YA FUE UTILIZADO EL SALDO.");
                } else {
                    boleta.setUsada(-2);
                    Calendar cal = DatatypeConverter.parseDateTime(revertBoletaRGMRequest.getPFechaHora().toString());
                    java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis());
                    boleta.setFechaHora(timestamp);
                    boleta.setUsuario(revertBoletaRGMRequest.getPUsuario());

                    try {
                        boletaRepository.save(boleta);
                        response.setEstado(0);
                        response.setDescripcion("SE REVERSO LA BOLETA EXITOSAMENTE.");

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

    @Override
    public GetUsuarioRGMResponse getUsuarioRGM(GetUsuarioRGMRequest getUsuarioRGMRequest) {

        final GetUsuarioRGMResponse response = new GetUsuarioRGMResponse();

        Persona persona = new Persona();

        VCodigoPersonas rugPersona = vCodigoPersonasRepository.findByCodigoRegistro(getUsuarioRGMRequest.getPCodigoPersona());

        if (rugPersona == null) {
            response.setPersona(persona);
            response.setEstado(2);
            response.setDescripcion("NO SE ENCONTRO UNA PERSONA CON ESTE CODIGO.");
        } else {
            RugPersonasFisicas rugPersonaF = personaRepository.findByIdPersona(rugPersona.getIdPersona());

            if (rugPersonaF == null) {
                response.setPersona(persona);
                response.setEstado(2);
                response.setDescripcion("NO SE ENCONTRO UNA PERSONA CON ESTE CODIGO.");
            } else {
                persona.setNit(rugPersona.getRfc());
                persona.setNombre(stripAccents(rugPersonaF.getNombrePersona()));
                if (getUsuarioRGMRequest.getPCodigoPersona().endsWith("I")) {
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

    @Override
    public SetBoletaRGMResponse setBoletaRGM(SetBoletaRGMRequest setBoletaRGMRequest) {
        final SetBoletaRGMResponse response = new SetBoletaRGMResponse();
        Float chequespropios = 0.0f;
        Float chequesotros = 0.0f;
        String noCheques = "NO";


        VCodigoPersonas rugPersona = vCodigoPersonasRepository.findByCodigoRegistro(setBoletaRGMRequest.getPCodigoPersona());

        if(rugPersona == null) {
            response.setEstado(2);
            response.setDescripcion("NO SE ENCONTRO UNA PERSONA CON ESTE CODIGO.");
        } else {
            Boleta boleta = boletaRepository.findByIdActivasNumero(setBoletaRGMRequest.getPNumero());

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

                    boletaRepository.UpdateSaldoBoletas(boleta.getMonto().intValue(),(int) rugPersona.getIdPersona(),1);
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
                bitacora.setAgencia(-3);
            } else {
                bitacora.setAgencia(Long.parseLong(setBoletaRGMRequest.getPAgencia(), 36));
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

    @Override
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

            Boleta boletaFinal = boletaRepository.findByBoleta(boleta.getIdTransaccion(), boleta.getSerie(), boleta.getNumero(), boleta.getMonto());

            if(boletaFinal!=null) {
                if(boletaFinal.getCodigoTramite()==3 && boletaFinal.getUsada() == 0) {
                    boletaFinal.setUsada(1);
                    boletaRepository.save(boletaFinal);
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

    private String stripAccents(String str) {
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

    private int diffTime(Timestamp fechaHora) {
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now(ZoneId.of("America/Guatemala"));
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);

        java.sql.Timestamp timestamp1 = java.sql.Timestamp.valueOf(tomorrowMidnight);

        long milliseconds = timestamp1.getTime() - fechaHora.getTime();
        int seconds = (int) milliseconds / 1000;

        int hours = seconds / 3600;

        return hours;
    }

}