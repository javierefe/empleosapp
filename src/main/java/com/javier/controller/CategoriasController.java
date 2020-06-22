package com.javier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javier.model.Categoria;
import com.javier.model.Vacante;
import com.javier.service.ICategoriasService;

@Controller
@RequestMapping(value="/categorias") //anotacion request a nivel de una clase
public class CategoriasController {
	
	@Autowired
	//@Qualifier("categoriasServiceJpa") //espicificar la implementacion por defecto, le dice a spring el nombre de la implementacion que queremos utilizar donde se haga una inyeccion
	private ICategoriasService serviceCategorias;

	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Categoria> lista = serviceCategorias.buscarTodas();
		model.addAttribute("categorias", lista);
		return "categorias/listCategorias";
	}

	@GetMapping("/create")
	public String crear(Categoria categoria) {
		return "categorias/formCategoria";
	}

	@PostMapping("/save")
	public String guardar(Categoria categoria,BindingResult result, RedirectAttributes attributes) {
		
		if (result.hasErrors()) {
//			for (ObjectError error: result.getAllErrors()){
//				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
//				}
			System.out.println("Existieron errores");
			return "categorias/formCategoria";
		}
		serviceCategorias.guardar(categoria);
		attributes.addFlashAttribute("msg", "registro guardado");
		System.out.println(categoria);
		
		return "redirect:/categorias/index";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idCategoria, RedirectAttributes attributes) {
		
		serviceCategorias.eliminar(idCategoria);
		
		attributes.addFlashAttribute("msg", "La categoria fue eliminada con exito");
		
		return "redirect:/categorias/index";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idCategoria, Model model) {
		
		Categoria categoria= serviceCategorias.buscarPorId(idCategoria);
		
		model.addAttribute("categorias", categoria);
		
		return "categorias/formCategoria";
	}

}
