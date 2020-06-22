package com.javier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javier.model.Vacante;

public interface VacantesRepository extends JpaRepository<Vacante, Integer>{

	//spring hace la implementacion  en tiempo de ejecucion
	List<Vacante> findByEstatus(String estatus);
	
	List<Vacante> findByDestacadoAndEstatusOrderByIdDesc(int destacado, String estatus);
	
	List<Vacante> findBySalarioBetweenOrderBySalarioDesc(double s1,double s2);
	
	List<Vacante> findByEstatusIn(String[] estatus);
}
