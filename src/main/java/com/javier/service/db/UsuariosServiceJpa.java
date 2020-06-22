package com.javier.service.db;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.javier.model.Usuario;
import com.javier.repository.UsuariosRepository;
import com.javier.service.IUsuariosService;

@Service
@Primary
public class UsuariosServiceJpa implements IUsuariosService{

	@Autowired
	private UsuariosRepository usuariosRepo;
	
	@Override
	public void guardar(Usuario usuario) {
		usuariosRepo.save(usuario);
		
	}

	@Override
	public void eliminar(Integer idUsuario) {
		usuariosRepo.deleteById(idUsuario);
		
	}

	@Override
	public List<Usuario> buscarTodos() {
		
		List<Usuario> lista = usuariosRepo.findAll();
		
		return lista;
	}

	@Override
	public Usuario buscarPorUsername(String username) {
		
		return usuariosRepo.findByUsername(username);
	}

}
