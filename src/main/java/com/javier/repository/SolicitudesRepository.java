package com.javier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.javier.model.Solicitud;

public interface SolicitudesRepository extends JpaRepository<Solicitud, Integer> {

}
