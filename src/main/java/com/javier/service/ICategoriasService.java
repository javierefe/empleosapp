package com.javier.service;

import java.util.List;

import com.javier.model.Categoria;

public interface ICategoriasService {

	void guardar(Categoria categoria);
	List<Categoria> buscarTodas();
	Categoria buscarPorId(Integer idCategoria);
	void eliminar(Integer idCategoria);
}
