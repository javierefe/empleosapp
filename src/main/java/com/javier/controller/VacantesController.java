package com.javier.controller;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javier.model.Vacante;
import com.javier.service.ICategoriasService;
import com.javier.service.IVacantesService;
import com.javier.util.Utileria;

@Controller
@RequestMapping(value = "vacantes")
public class VacantesController {

	@Value("${empleosapp.ruta.imagenes}")
	private String ruta;

	@Autowired
	private IVacantesService serviceVacantes;

	@Autowired
	@Qualifier("categoriasServiceJpa")
	private ICategoriasService serviceCategorias;

	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Vacante> lista = serviceVacantes.buscarTodas();
		model.addAttribute("vacante", lista);
		return "vacantes/listVacantes";
	}

	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Vacante> lista = serviceVacantes.buscarTodas(page);
		model.addAttribute("vacantes", lista);
		return "vacantes/listVacantes";
	} 
	
	
	//Para vincular los errores en la vista, tambien  debemos enviar al formulario un objeto de nuestra clase 	de modelo(se declara como parametro)
	//Vacante vacante
	@GetMapping("/create")
	public String crear(Vacante vacante, Model model) {
		//enviamos categorias para mostrar en fomularios todas las categorias al momento de crear la vacante , se uso la anotacion @ModelAtribute
		
		return "vacantes/formVacantes";
	}

	@PostMapping("/save")
	public String guardar(Vacante vacante, BindingResult result, RedirectAttributes attributes, Model model,
			@RequestParam("archivoImagen") MultipartFile multiPart) {

		if (result.hasErrors()) {
			
			System.out.println("Ocurrio un error ");
			
			return "vacantes/formVacantes";
		}

		if (!multiPart.isEmpty()) {
			// String ruta = "/empleos/img-vacantes/"; // Linux/MAC
			// String ruta = "c:/empleos/img-vacantes/"; // Windows
			String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
			if (nombreImagen != null) { // La imagen si se subio
				// Procesamos la variable nombreImagen
				vacante.setImagen(nombreImagen);
			}
		}
		serviceVacantes.guardar(vacante);
		attributes.addFlashAttribute("msg", "Registro guardado");
		System.out.println(vacante);

		return "redirect:/vacantes/index";
	}

//	@PostMapping("/save")
//	public String guardar ( @RequestParam("nombre") String nombre, @RequestParam("descripcion") String descripcion,
//			@RequestParam("estatus") String estatus,@RequestParam("fecha") String fecha, @RequestParam("destacado") int destacado, 
//			@RequestParam("salario") double salario, @RequestParam("detalles") String detalles) {
//		
//		return "vacantes/listVacantes";
//	}
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idVacante, RedirectAttributes attributes) {
		System.out.println("Borrando vacante con id: " + idVacante);
		
		serviceVacantes.eliminar(idVacante);
		
		attributes.addFlashAttribute("msg", "La vacante fue eliminada con exito");
		
		return "redirect:/vacantes/index";
	}

	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idVacante, Model model) {
		
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		
		model.addAttribute("vacante", vacante);
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
		
		return "vacantes/formVacantes";
	}
	
	@GetMapping("/view/{id}")
	public String verDetalle(@PathVariable("id") int idVacante, Model model) {

		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		model.addAttribute("vacante", vacante);

		// buscar los detalles de la vacante en la base de datos

		return "detalle";
	}

	/*@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		webDataBinder.registerCustomEditor(LocalDate.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy") ,true));
	}
	*/

	
	//Este nos metodo nos permite agregar datos al modelo, datos comunes para todos los metodos
	@ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
	}
//	
//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//		binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
//			@Override
//			public void setAsText(String text) throws IllegalArgumentException {
//				setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//			}
//
//			@Override
//			public String getAsText() throws IllegalArgumentException {
//				return DateTimeFormatter.ofPattern("dd-MM-yyyy").format((LocalDate) getValue());
//			}
//		});
//	}

//	@GetMapping("/detalle")
//	public String verDetalle2(@RequestParam("idVacante") int idVacante, Model model) {
//		
//		System.out.println("RequestParam: " + idVacante);
//		model.addAttribute("idVacante", idVacante);
//		return "vacantes/detalle";
//	} 	 	
}
