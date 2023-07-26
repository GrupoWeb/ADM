package gt.gob.rgm.adm.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;

import gt.gob.rgm.adm.dao.UsuarioRepository;
import gt.gob.rgm.adm.exception.EntityAlreadyExistsException;
import gt.gob.rgm.adm.model.Usuario;
import gt.gob.rgm.adm.util.CryptoUtils;

@Stateless
public class UsuarioServiceImp implements UsuarioService {
	
	@Inject
	private UsuarioRepository usuarioDao;

	@Override
	public int update(Long id, Usuario usuario) {
		Usuario usuarioEncontrado = usuarioDao.findById(id);
		if(usuarioEncontrado != null) {
			// actualizar campos
			if(usuario.getEstado() != null) {
				usuarioEncontrado.setEstado(usuario.getEstado());
			}
			if(usuario.getPassword() != null) {
				usuarioEncontrado.setPassword(usuario.getPassword());
			}
			if(usuario.getRol() != null) {
				usuarioEncontrado.setRol(usuario.getRol());
			}
			usuarioDao.save(usuarioEncontrado);
			return 1;
		} else {
			// usuario no encontrado
			return 0;
		}
	}
	
	@Override
	public Usuario save(Usuario usuario) throws EntityAlreadyExistsException {
		// verificar si el correo no existe
		Usuario existente = usuarioDao.findByEmail(usuario.getEmail());
		if(existente != null) {
			throw new EntityAlreadyExistsException("El correo ya está siendo utilizado por otro usuario.");
		}
		// generar password aleatorio
		String password = RandomStringUtils.randomAlphanumeric(8);
		usuario.setPassword(CryptoUtils.hash(password, new StringBuffer(usuario.getEmail()).reverse().toString()));
		usuario.setCreado(Timestamp.valueOf(LocalDateTime.now()));
		usuario.setEstado("A");
		usuarioDao.save(usuario);
		
		// clonar usuario y asignarle el password en texto plano
		Usuario clonado = Usuario.clonar(usuario);
		clonado.setPassword(password);
		return clonado;
	}

	@Override
	public List<Usuario> listUsuarios(String estado) {
		return usuarioDao.findByStatusOrderedByNombre(estado);
	}

	@Override
	public Usuario getUsuario(Long id) {
		return usuarioDao.findById(id);
	}
}
