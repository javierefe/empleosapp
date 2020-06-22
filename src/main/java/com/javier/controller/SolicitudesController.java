package com.javier.controller;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javier.model.Solicitud;
import com.javier.model.Usuario;
import com.javier.model.Vacante;
import com.javier.service.ISolicitudesService;
import com.javier.service.IUsuariosService;
import com.javier.service.IVacantesService;
import com.javier.util.Utileria;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudesController {

	@Autowired
	private ISolicitudesService serviceSolicitudes;

	@Autowired
	private IVacantesService serviceVacantes;

	@Autowired
	private IUsuariosService serviceUsuarios;
	
	/**
	 * EJERCICIO: Declarar esta propiedad en el archivo application.properties. El
	 * valor sera el directorio en donde se guardarán los archivos de los
	 * Curriculums Vitaes de los usuarios.
	 */
	@Value("${empleosapp.ruta.cv}")
	private String ruta;

	/**
	 * Metodo que muestra la lista de solicitudes sin paginacion Seguridad: Solo
	 * disponible para un usuarios con perfil ADMINISTRADOR/SUPERVISOR.
	 * 
	 * @return
	 */
	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Solicitud> lista = serviceSolicitudes.buscarTodas();
		model.addAttribute("solicitudes", lista);
		return "solicitudes/listSolicitudes";

	}

	/**
	 * Metodo que muestra la lista de solicitudes con paginacion Seguridad: Solo
	 * disponible para usuarios con perfil ADMINISTRADOR/SUPERVISOR.
	 * 
	 * @return
	 */
	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Solicitud> lista = serviceSolicitudes.buscarTodas(page);
		model.addAttribute("solicitudes", lista);
		return "solicitudes/listSolicitudes";

	}

	/**
	 * Método para renderizar el formulario para aplicar para una Vacante Seguridad:
	 * Solo disponible para un usuario con perfil USUARIO.
	 * 
	 * @return
	 */

	// Se envia como parametro el objeto solicitud para vincular los posibles
	// errores

	@GetMapping("/create/{idVacante}")
	public String crear(Solicitud solicitud, @PathVariable Integer idVacante, Model model) {

		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		model.addAttribute("vacante", vacante);
		return "solicitudes/formSolicitud";

	}

	/**
	 * Método que guarda la solicitud enviada por el usuario en la base de datos
	 * Seguridad: Solo disponible para un usuario con perfil USUARIO.
	 * @return
	 */
	@PostMapping("/save")
	public String guardar(Solicitud solicitud, BindingResult result, RedirectAttributes attributes, Model model, HttpSession session,
			@RequestParam("archivoCV") MultipartFile multiPart, Authentication authentication) {	
		
		// Recuperamos el username que inicio sesión
		String username = authentication.getName();
		
		if(result.hasErrors()) {
			System.out.println("Ocurrio un error");
			return "solicitudes/formSolicitud";
		}
		if(!multiPart.isEmpty()) {
			
			String nombreArchivo = Utileria.guardarArchivo(multiPart, ruta);
			if(nombreArchivo != null) {
				solicitud.setArchivo(nombreArchivo);
			}
		}
		
		Usuario usuario = serviceUsuarios.buscarPorUsername(username);
		solicitud.setUsuario(usuario);
		solicitud.setFecha(new Date());
		
	
		serviceSolicitudes.guardar(solicitud);
		
		return "redirect:/";	
		
	}
	@GetMapping("/edit/{id}")
	public String editar(Solicitud solicitud, @PathVariable("id") int idVacante, Model model) {
		
		//serviceSolicitudes.buscarPorId(idSolicitud);
		
		
		model.addAttribute("vacante", serviceVacantes.buscarPorId(idVacante) );
		//model.addAttribute("categorias", serviceCategorias.buscarTodas());
		
		return "solicitudes/formSolicitud";
	}

	/**
	 * Método para eliminar una solicitud Seguridad: Solo disponible para usuarios
	 * con perfil ADMINISTRADOR/SUPERVISOR.
	 * 
	 * @return
	 */
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idSolicitud, RedirectAttributes attributes) {

		// EJERCICIO
		serviceSolicitudes.eliminar(idSolicitud);
		attributes.addAttribute("msg", "La solicitud fue eliminada con exito");
		//return "redirect:/solicitudes/indexPaginate";
		return "redirect:/solicitudes/index";

	}

	/**
	 * Personalizamos el Data Binding para todas las propiedades de tipo Date
	 * 
	 * @param webDataBinder
	 */
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
//	@InitBinder
//	public void InitBinder(WebDataBinder binder) {
//		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
//		//la clase StringTrimerEditor si recive true como parametro cambia el string a null
//	}

}
