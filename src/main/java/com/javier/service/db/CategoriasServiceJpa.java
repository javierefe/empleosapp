package com.javier.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.javier.model.Categoria;
import com.javier.repository.CategoriasRepository;
import com.javier.service.ICategoriasService;

@Service
//cuando se inyecte una instancia de nuestra variable en el controlador se usara esta implementacion ya que antes no se sabia cual usar CategoriaServiceImpl o CategorisServiceJpa
@Primary
public class CategoriasServiceJpa implements ICategoriasService {

	@Autowired
	private CategoriasRepository categoriasRepo;

	@Override
	public void guardar(Categoria categoria) {
		categoriasRepo.save(categoria);
	}

	@Override
	public List<Categoria> buscarTodas() {

		List<Categoria> lista = categoriasRepo.findAll();

		return lista;
	}

	@Override
	public Categoria buscarPorId(Integer idCategoria) {
		Optional<Categoria> optional = categoriasRepo.findById(idCategoria);

		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public void eliminar(Integer idCategoria) {
		categoriasRepo.deleteById(idCategoria);
		
	}

}
