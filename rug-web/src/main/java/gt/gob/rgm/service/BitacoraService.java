package gt.gob.rgm.service;

import gt.gob.rgm.dao.BitacoraConsultaRepository;
import gt.gob.rgm.model.BitacoraConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class BitacoraService {

    @Autowired
    private BitacoraConsultaRepository bitacoraConsultaRepository;

    @Autowired
    private HttpServletRequest request;

    public void registrarOperacion(String operacion, String detalle, Long usuarioId) {
        BitacoraConsulta bitacora = new BitacoraConsulta();
        bitacora.setOperacion(operacion);
        bitacora.setDetalle(detalle);
        bitacora.setUsuarioId(usuarioId);
        bitacora.setIpAddress(request.getRemoteAddr());
        bitacora.setUserAgent(request.getHeader("User-Agent"));

        bitacoraConsultaRepository.save(bitacora);
    }
}
