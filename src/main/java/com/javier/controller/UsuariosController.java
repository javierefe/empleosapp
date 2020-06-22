package com.javier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javier.model.Usuario;
import com.javier.service.IUsuariosService;

@Controller
@RequestMapping("usuarios")
public class UsuariosController {

	@Autowired
	private IUsuariosService serviceUsuario;
	
	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		
		List<Usuario> lista = serviceUsuario.buscarTodos();
		
		model.addAttribute("usuarios", lista);
		return "usuarios/listUsuarios";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {
		
		serviceUsuario.eliminar(idUsuario);
		
		attributes.addFlashAttribute("msg", "Usuario eliminado con exxito");
		return "redirect:/usuarios/index";
	}
}
