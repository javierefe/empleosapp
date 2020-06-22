package com.javier.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.javier.model.Vacante;

public interface IVacantesService {

	List<Vacante> buscarTodas();
	Vacante buscarPorId(Integer id);
	void guardar(Vacante vacante);
	List<Vacante> buscarDestacadas();
	void eliminar(Integer idVacante);
	List<Vacante> buscarByExample(Example<Vacante> example); //INTERFAZ EXAMPLE , consulta por muestra-->query by example 
	//List<Vacante> buscarByDescripci
	Page<Vacante> buscarTodas(Pageable page);
}
