package gt.gob.rgm.adm.service;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import gt.gob.rgm.adm.dao.UsuarioRepository;
import gt.gob.rgm.adm.domain.Principal;
import gt.gob.rgm.adm.model.Usuario;
import gt.gob.rgm.adm.util.Constants;
import gt.gob.rgm.adm.util.CryptoUtils;
import gt.gob.rgm.adm.util.JWTUtils;

@Stateless
public class SecurityServiceImp implements SecurityService {
	
	@Inject
	UsuarioRepository usuarioDao;

	@Override
	public Principal login(Principal principal) {
		Usuario usuarioActual = usuarioDao.findByEmail(principal.getEmail());
		
		if(usuarioActual == null) {
			principal.setCode(Principal.USER_NOT_FOUND);
		} else {
			principal.setPassword(CryptoUtils.hash(principal.getPassword(), new StringBuffer(principal.getEmail()).reverse().toString()));
			if(usuarioActual.getPassword().equals(principal.getPassword())) {
                        //if(1==1) {
				principal.setUsuarioId(usuarioActual.getUsuarioId());
				principal.setNombre(usuarioActual.getNombre());
				principal.setRol(usuarioActual.getRol());
				switch(usuarioActual.getEstado()) {
					case "A":
						principal.setCode(Principal.LOGIN_SUCCESS);
						Map<String, Object> payload = new HashMap<>();
						payload.put("nombre", principal.getNombre());
						payload.put("email", principal.getEmail());
						String tokenGenerado = JWTUtils.generateToken(principal.getEmail(), payload, Constants.SESSION_DURATION);
						principal.setToken(tokenGenerado);
						break;
					case "H":
						principal.setCode(principal.USER_TEMPORARILY_DISABLED);
						break;
					case "I":
						principal.setCode(principal.USER_DISABLED);
						break;
				}
			} else {
				principal.setCode(principal.AUTHENTICATION_FAILED);
			}
		}
		principal.setPassword("");
		return principal;
	}
	
	public static void main(String[] args) {
		String email = "csolares@mineco.gob.gt";
		String password = "Usuario1";
		
		String hashed = CryptoUtils.hash(password, new StringBuffer(email).reverse().toString());
		System.out.println(hashed);
	}
}
