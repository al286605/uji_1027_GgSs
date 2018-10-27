package es.uji.ei1027.GgSs.controller;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.uji.ei1027.GgSs.dao.AsignacionDao;
import es.uji.ei1027.GgSs.dao.BtcDao;
import es.uji.ei1027.GgSs.dao.EmpresaDao;
import es.uji.ei1027.GgSs.dao.EstudianteDao;
import es.uji.ei1027.GgSs.dao.ItinerariosDao;
import es.uji.ei1027.GgSs.dao.Oferta_de_proyectoDao;
import es.uji.ei1027.GgSs.dao.Oferta_en_listaDao;
import es.uji.ei1027.GgSs.dao.PersonaDeContactoDao;
import es.uji.ei1027.GgSs.dao.TutorDao;
import es.uji.ei1027.GgSs.dao.UsuarioDao;
import es.uji.ei1027.GgSs.modelo.Asigna;
import es.uji.ei1027.GgSs.modelo.Btc;
import es.uji.ei1027.GgSs.modelo.Empresa;
import es.uji.ei1027.GgSs.modelo.Estudiante;
import es.uji.ei1027.GgSs.modelo.Itinerario;
import es.uji.ei1027.GgSs.modelo.Oferta_de_proyecto;
import es.uji.ei1027.GgSs.modelo.PersonaDeContacto;
import es.uji.ei1027.GgSs.modelo.Tutor;
import es.uji.ei1027.GgSs.modelo.Usuario;



@Controller
@RequestMapping("/btc")
public class BtcController {
	private String raiz = "/btc";

	private EmpresaDao empresaDao;
	private UsuarioDao usuarioDao;
	private PersonaDeContactoDao personaDao;
	private Oferta_de_proyectoDao ofertaDao;
	private ItinerariosDao itinerariosDao;
	private BtcDao btcDao;
	private TutorDao tutorDao;
	private AsignacionDao asignaDao;
	private EstudianteDao estudianteDao;
	private Oferta_en_listaDao listaDao;

	@Autowired
    public void setEmpresaDao(EmpresaDao dao) { 
        this.empresaDao=dao;
    }
	@Autowired
    public void setUsuarioDao(UsuarioDao dao) { 
        this.usuarioDao=dao;
    }
    @Autowired
    public void setPersonaDeContactoDao(PersonaDeContactoDao dao) { 
        this.personaDao=dao;
    }
    @Autowired
    public void setOferta_de_proyectoDao(Oferta_de_proyectoDao dao) { 
        this.ofertaDao=dao;
    }
    @Autowired
    public void setItinerariosDao(ItinerariosDao dao) { 
        this.itinerariosDao=dao;
    }
    @Autowired
    public void setBtcDao(BtcDao dao) { 
        this.btcDao=dao;
    }
    @Autowired
    public void setTutorDao(TutorDao dao) { 
        this.tutorDao=dao;
    }
    @Autowired
    public void setAsignaDao(AsignacionDao dao) { 
        this.asignaDao=dao;
    }
    @Autowired
    public void setEstudianteDao(EstudianteDao dao) { 
        this.estudianteDao=dao;
    }
    @Autowired
    public void setListaDao(Oferta_en_listaDao dao) { 
        this.listaDao=dao;
    }
    
    private boolean puedeAsignar(String alias) {
    	Asigna asig;
    	try {
    		asig = asignaDao.getAsignaPendiente(alias);
    		return false;
    	}catch(EmptyResultDataAccessException e)
    	{
    		try {
    			asig = asignaDao.getAsignaAceptada(alias);
        		return false;
    		}catch(EmptyResultDataAccessException ex) {
        		try {
        			asig = asignaDao.getAsignaTraspasada(alias);
            		return false;
        		}catch(EmptyResultDataAccessException exp) {}
    		}
    	}
    	return true;
    }

    
    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    
    

    @RequestMapping("") 
    public String none(Model model) {
        return "redirect:" +raiz + "/";
    }    

    @RequestMapping("/") 
    public String index(Model model) {
        return "redirect:" +raiz + "/ofertes.html";
    }

    @RequestMapping("/index") 
    public String viewOptions(Model model, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
        return raiz + "/index";
    }

    @RequestMapping("/ofertes") 
    public String viewOfertas(Model model, HttpSession session) { 
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	String visible = (String) session.getAttribute("visible");
        model.addAttribute("visible", visible);
        session.removeAttribute("visible");
        
        String oculta = (String) session.getAttribute("oculta");
        model.addAttribute("oculta", oculta);
        session.removeAttribute("oculta");
    	
        model.addAttribute("solicituts", ofertaDao.getOfertas_de_proyecto());
        return raiz + "/ofertas_listadas";
    }
    
    @RequestMapping(value="/oferta/{id}", method = RequestMethod.GET) 
    public String viewEstudiante(Model model, @PathVariable int id, HttpSession session) {
    	Oferta_de_proyecto oferta = ofertaDao.getOferta_de_proyecto(id);
        model.addAttribute("oferta", oferta);
        session.setAttribute("oferta", oferta);
        return raiz + "/veure_oferta"; 
    }
    
    @RequestMapping(value="/oculta", method = RequestMethod.GET) 
    public String processHide(HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("dcc") && !user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	Oferta_de_proyecto oferta = (Oferta_de_proyecto)session.getAttribute("oferta");
    	if(oferta == null)
    		return "/error";
    	oferta.setEstado("Acceptada");
        ofertaDao.updateOferta_de_proyecto(oferta);
        session.removeAttribute("oferta");
        session.setAttribute("oculta", oferta.getTitulo());
        return "redirect:"+raiz + "/ofertes.html"; 
    }
    
    @RequestMapping(value="/fer_visible", method = RequestMethod.GET) 
    public String processAcceptSubmit(HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("dcc") && !user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	Oferta_de_proyecto oferta = (Oferta_de_proyecto)session.getAttribute("oferta");
    	if(oferta == null)
    		return "/error";
    	oferta.setEstado("Visible per alumnes");
        ofertaDao.updateOferta_de_proyecto(oferta);
        session.removeAttribute("oferta");
        session.setAttribute("visible", oferta.getTitulo());
        return "redirect:"+raiz + "/ofertes.html"; 
    }

    @RequestMapping("/preferencies_alumnes") 
    public String viewPreferencias(Model model, HttpSession session) { 
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
        model.addAttribute("alumnos", estudianteDao.getEstudiantes_con_preferencias());
        return raiz + "/alumnos_con_preferencias";
    }

    @RequestMapping(value="/preferencies_alumnes/{alumno}") 
    public String redirectPreferencias(@PathVariable String alumno) { 
    	return "redirect:" + raiz + "/preferencies_alumnes/" + alumno + "/";
    }

    @RequestMapping(value="/preferencies_alumnes/{alumno}/", method = RequestMethod.GET) 
    public String viewPreferencias(Model model, @PathVariable String alumno, HttpSession session) { 
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	model.addAttribute("alumno", estudianteDao.getEstudiante(alumno));
        model.addAttribute("solicituts", listaDao.getOfertas(alumno));
        return raiz + "/preferencias_alumnos";
    }
    
    @RequestMapping(value="/preferencies_alumnes/{alumno}/{id}", method = RequestMethod.GET) 
    public String viewPreferencia(Model model, @PathVariable String alumno, @PathVariable int id, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	model.addAttribute("permiso_asignar", puedeAsignar(alumno));
    	model.addAttribute("alumno", estudianteDao.getEstudiante(alumno));
        model.addAttribute("oferta", listaDao.getOferta(id, alumno));
        model.addAttribute("lista_tutores", tutorDao.getTutores());
        model.addAttribute("asignacion", new Asigna());
        return raiz + "/asignar_oferta"; 
    }

    
    @RequestMapping(value="/preferencies_alumnes/{alumno}/{id}", method = RequestMethod.POST) 
    public String setPreferencia(Model model, @PathVariable String alumno,
    		@PathVariable int id, HttpSession session,
    		@ModelAttribute("asignacion") Asigna asignacion) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	model.addAttribute("alumno", estudianteDao.getEstudiante(alumno));
        model.addAttribute("oferta", listaDao.getOferta(id, alumno));
        model.addAttribute("tut", tutorDao.getTutor(asignacion.getProfesor()));
        model.addAttribute("asignacion", asignacion);
        asignacion.setAlumno(alumno);
        asignacion.setProyecto(id);
        asignacion.setEstado("pendiente");
        session.setAttribute("asignacion", asignacion);
        return raiz + "/confirmar_asignacio_oferta"; 
    }

    @RequestMapping("/confirmar_asignacio_oferta") 
    public String addOfer(Model model, HttpSession session) { 
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	
    	Asigna 	asignacion = (Asigna)session.getAttribute("asignacion");
    	try {
    		asignaDao.getAsigna(asignacion.getAlumno(), asignacion.getProyecto());
    		asignaDao.updateAsignaPropuesta( asignacion.getProyecto(), asignacion.getAlumno());
    	}catch(EmptyResultDataAccessException e) {
    		asignaDao.addAsigna(asignacion);
    	}
    	
    	Oferta_de_proyecto ofer = ofertaDao.getOferta_de_proyecto(asignacion.getProyecto());
    	ofer.setEstado("Assignada");
    	ofertaDao.updateOferta_de_proyecto(ofer);
    	model.addAttribute("alumno", estudianteDao.getEstudiante(asignacion.getAlumno()));
        model.addAttribute("oferta", listaDao.getOferta(asignacion.getProyecto(), asignacion.getAlumno()));
        model.addAttribute("tut", tutorDao.getTutor(asignacion.getProfesor()));
        model.addAttribute("asignacion", asignacion);
        session.removeAttribute("asignacion");
        return raiz + "/asignacion_realizada";
    }
    
    @RequestMapping(value="/asignar/{alias}", method = RequestMethod.GET) 
    public String asignar(Model model, @PathVariable String alias, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("dcc") && !user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	Btc btc = (Btc)session.getAttribute("asignacion");
    	if(btc == null)
    		return "/error";
    	Asigna asignacion = new Asigna();
    	asignacion.setAlumno(btc.estudiante.getAlias());
    	asignacion.setEstado("");
    	asignacion.setProfesor(alias);
    	asignacion.setProyecto(btc.oferta.getId());
    	asignaDao.addAsigna(asignacion);
        session.removeAttribute("asignacion");
        return raiz + "/veure_oferta"; 
    }

    @RequestMapping("/asignacions") 
    public String viewAsignaciones(Model model, HttpSession session) { 
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
        model.addAttribute("asignaciones", asignaDao.getAsignaciones());
        return raiz + "/asignaciones";
    }

    @RequestMapping("/asignacio/{alumne}/{id}") 
    public String viewAsignacio(Model model,@PathVariable String alumne,@PathVariable int id, HttpSession session) { 
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	Asigna asigna = asignaDao.getAsigna(alumne, id);
        model.addAttribute("asigna", asigna);
        model.addAttribute("alumno", estudianteDao.getEstudiante(asigna.getAlumno()));
        model.addAttribute("oferta", listaDao.getOferta(asigna.getProyecto(), asigna.getAlumno()));
        session.setAttribute("asigna", asigna);
        return raiz + "/ver_asignacio";
    }

    
    @RequestMapping(value="/confirmar", method = RequestMethod.GET) 
    public String confirmar(Model model, @PathVariable String alias, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("dcc") && !user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	Asigna asigna = (Asigna)session.getAttribute("asigna");
    	if(asigna == null)
    		return "/error";
    	asignaDao.checkAsigna(asigna.getProyecto(), asigna.getAlumno());
        session.removeAttribute("asignacion");
        return "redirect:"+ raiz + "/asignacions.html"; 
    }
    @RequestMapping(value="/iglu", method = RequestMethod.GET) 
    public String iglu(Model model,  HttpSession session) {
      Usuario user = (Usuario)session.getAttribute("usuario");
      if(user == null || (!user.getRol().equals("dcc") && !user.getRol().equals("btc") && !user.getRol().equals("groot")))
        return "/no_acces";
      Asigna asigna = (Asigna)session.getAttribute("asigna");
      if(asigna == null)
        return "/error";
      asignaDao.moveIGLUAsigna(asigna.getProyecto(), asigna.getAlumno());
        session.removeAttribute("asigna");
        return "redirect:"+ raiz + "/asignacio/" + asigna.getAlumno() +"/"+asigna.getProyecto(); 
    }

}
