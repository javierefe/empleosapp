package com.javier.service;

import java.util.List;

import com.javier.model.Usuario;

public interface IUsuariosService {
	
	void guardar(Usuario usuario);
	void eliminar(Integer idUsuario);
	List<Usuario> buscarTodos();
	Usuario buscarPorUsername(String username);

}
