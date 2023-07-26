package gt.gob.rgm.adm.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.BitacoraOperacionesRepository;
import gt.gob.rgm.adm.dao.UsuarioRepository;
import gt.gob.rgm.adm.model.BitacoraOperaciones;
import gt.gob.rgm.adm.model.Usuario;
import gt.gob.rgm.adm.util.JWTUtils;

@Stateless
public class BitacoraOperacionesServiceImp implements BitacoraOperacionesService {
	
	@Inject
	private BitacoraOperacionesRepository bitacoraDao;
	
	@Inject
	private UsuarioRepository usuarioDao;
	
	@Override
	public long createEntry(String token, String message) {
		// decodificar token para obtener id
		String emailDecoded = JWTUtils.decodeToken(token);
		Usuario usuario = usuarioDao.findByEmail(emailDecoded);
		if(usuario != null) {
			BitacoraOperaciones bitacora = new BitacoraOperaciones();
			bitacora.setUsuarioId(usuario.getUsuarioId());
			bitacora.setOperacion(message);
			bitacora.setFecha(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
			bitacoraDao.save(bitacora);
			return usuario.getUsuarioId();
		}
		return -1;
	}
}
