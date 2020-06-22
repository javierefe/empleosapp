package com.javier.controller;


import java.time.LocalDate;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javier.model.Perfil;
import com.javier.model.Usuario;
import com.javier.model.Vacante;
import com.javier.service.ICategoriasService;
import com.javier.service.IUsuariosService;
import com.javier.service.IVacantesService;

@Controller
public class HomeController {
	
	@Autowired
	private IVacantesService serviceVacantes;
	
	@Autowired
	private IUsuariosService serviceUsuarios;
	
	@Autowired
	private ICategoriasService serviceCategorias;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/login")
	public String mostrarLogin() {
		
		return "formLogin";
	}
	
	@GetMapping("/signup")
	public String registrarse(Usuario usuario, Model model) {
		
		
		return "formRegistro";
	}
	
	@PostMapping("/signup")
	public String guardarRegistro(Usuario usuario, RedirectAttributes attributes) {
		String psw = usuario.getPassword();
		String pswEn = passwordEncoder.encode(psw);
		
		usuario.setPassword(pswEn);
		
		usuario.setEstatus(1);
		usuario.setFechaRegistro(LocalDate.now());
		
		Perfil perfil = new Perfil();
		perfil.setId(3);
		usuario.agregar(perfil);
		
		serviceUsuarios.guardar(usuario);
		
		attributes.addAttribute("msg", "Usuario guardado con rxito :v");
		return "redirect:/usuarios/index";
	}
	
	@GetMapping("/logout") 
	public String logout(HttpServletRequest request){
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/login";
		
	}

	@GetMapping("/tabla")
	public String mostrarTabla(Model model){
		
		List<Vacante> lista = serviceVacantes.buscarTodas();
		model.addAttribute("vacantes", lista);
		return "tabla";
		
	}
	
//	@GetMapping("/detalle")
//	public String mostrarDetalle(Model model) {
//		Vacante vacante = new Vacante();
//		vacante.setNombre("Ingeniero de Comunicaciones");
//		vacante.setDescripcion("Se solicita ingeniero para dar soporte a intranet");
//		vacante.setFecha(new Date ());
//		vacante.setSalario(9700.0);
//		model.addAttribute("vacante", vacante);
//		return "detalle";
//	}
	
//	@GetMapping("/listado")
//	public String mostrarListado(Model model) {
//		List<String> lista = new LinkedList<String>();
//		lista.add("Ingeniero de Sistemas");
//		lista.add("Auxiliar de Contabilidad");
//		lista.add("Vendedor");
//		lista.add("Arquitecto");
//		
//		model.addAttribute("empleos",lista);
//		return "listado";
//	}
	@GetMapping("/")// respondera a una peticion de tipo get, el / representa al directorio raiz de nuestra aplicacion
	public String mostrarHome(Model model) {
		
		List<Vacante> lista = serviceVacantes.buscarDestacadas();
		model.addAttribute("vacantes", lista);
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
		return "home";
	}
	
	@GetMapping("/index")
	public String mostrarIndex(Authentication auth, HttpSession session) {
		//Authentication --> interfaz que nos permite recuperar informacion del usuario que inicia sesion
			String username = auth.getName(); //recuperamos el nombre del usuario
			System.out.println("Nombre del usuario: " + username	);
			
			for (GrantedAuthority rol : auth.getAuthorities()) {
				System.out.println("ROL: " + rol.getAuthority());
			}
			
			if(session.getAttribute("usuario") == null) {
				Usuario usuario = serviceUsuarios.buscarPorUsername(username);
				usuario.setPassword(null);
				//para agregar datos a la sesion agregar el parametro --> HttpSession
				System.out.println("Usuario: " + usuario);
				session.setAttribute("usuario", usuario);
			}
			
			
		return "redirect:/";
	}
	
	@GetMapping("/search")
	public String buscar( @ModelAttribute("search") Vacante vacante, Model model) {
		System.out.println("buscando por: " + vacante);
		
		//PARA QUE NO USE EL OPERadoR = y en si lugar se use la palabra reservada LIKE 
		//where descripcion like %?%
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("descripcion" , ExampleMatcher.GenericPropertyMatchers.contains());
		
		
		Example<Vacante> example = Example.of(vacante, matcher); //una variable de tipo example que reciba como muestra un objeto de tipo vacante
		List<Vacante> lista = serviceVacantes.buscarByExample(example);
		model.addAttribute("vacantes", lista);
		return "home";
	}
	
	@GetMapping("/bcrypt/{texto}")
	@ResponseBody //anotacion usada para cuando queremos que el return renderize como texto y no busque una plantillas
	public String encriptado(@PathVariable("texto") String texto) {
		
		return texto + " Encriptado con Bcrypt: " + passwordEncoder.encode(texto); 
	}
	//este metodo es para que los tipo string cuando se  detecte vacios en el data binding se cambien a null
	@InitBinder
	public void InitBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		//la clase StringTrimerEditor si recive true como parametro cambia el string a null
	}
	
	@ModelAttribute
	public void setGenericos(Model model) {
		Vacante vacanteSearch = new Vacante();
		vacanteSearch.reset();
		model.addAttribute("vacantes", serviceVacantes.buscarDestacadas());
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
		model.addAttribute("search", vacanteSearch);
		
	}
}
