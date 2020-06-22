package com.javier.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javier.model.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer>{

	Usuario findByUsername(String username);
}
