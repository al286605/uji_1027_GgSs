package es.uji.ei1027.GgSs.controller;

import java.util.ArrayList;
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
import es.uji.ei1027.GgSs.dao.Preferencia_alumnoDao;
import es.uji.ei1027.GgSs.dao.TutorDao;
import es.uji.ei1027.GgSs.dao.UsuarioDao;
import es.uji.ei1027.GgSs.modelo.Asigna;
import es.uji.ei1027.GgSs.modelo.Estudiante;
import es.uji.ei1027.GgSs.modelo.Itinerario;
import es.uji.ei1027.GgSs.modelo.Oferta_de_proyecto;
import es.uji.ei1027.GgSs.modelo.Preferencia_alumno;
import es.uji.ei1027.GgSs.modelo.Usuario;


@Controller
@RequestMapping("/alumne")
public class EstudianteController {
	private String raiz = "/alumne";
	
	private EstudianteDao estudianteDao; 
	private EmpresaDao empresaDao;
	private UsuarioDao usuarioDao;
	private PersonaDeContactoDao personaDao;
	private Oferta_de_proyectoDao ofertaDao;
	private Oferta_en_listaDao oferta_listaDao;
	private ItinerariosDao itinerariosDao;
	private BtcDao btcDao;
	private TutorDao tutorDao;
	private AsignacionDao asignaDao;
	private Preferencia_alumnoDao preferenciaDao;

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
    public void setOferta_en_listaDao(Oferta_en_listaDao dao) { 
        this.oferta_listaDao=dao;
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
    public void setEstudianteDao(EstudianteDao estudianteDao) { 
        this.estudianteDao=estudianteDao;
    }
    @Autowired
    public void setPreferenciaDao(Preferencia_alumnoDao dao) { 
        this.preferenciaDao=dao;
    }
    
    private boolean puedePriorizar(String alias) {
    	Asigna asig;
    	try {
    		asig = asignaDao.getAsignaPendiente(alias);
    		return false;
    	}catch(EmptyResultDataAccessException e)
    	{
    		try {
    			asig = asignaDao.getAsignaAceptada(alias);
        		return false;
    		}catch(EmptyResultDataAccessException ex) {}
    	}
    	return true;
    }

    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    
    @RequestMapping("") 
    public String none(Model model) {
        return "redirect:" +raiz + "/";
    }    
    
    public void addStudents()
    {
    	int primer_alumno = 203481;
    	for(int i=primer_alumno; i<primer_alumno+20;i++) {    		
    		String alias = "al" +i;
    		Usuario user = new Usuario();
    		user.setAlias(alias);
    		user.setContrasenya(alias);
    		user.setCorreo(alias+"@uji.es");
    		user.setRol("alumno");
    		usuarioDao.addUsuario(user);
    		Estudiante estudiante = new Estudiante();
    		estudiante.setAlias(alias);
    		estudiante.setAnyo_academico(2018);
    		estudiante.setNombre(String.valueOf(i));
    		estudiante.setApellido(String.valueOf(String.valueOf(i).hashCode()));
    		estudiante.setDni(alias+"D");
    		estudiante.setNumero_de_creditos_aprobados(222);
    		estudiante.setTelefono("600"+String.valueOf(i));
    		List<Itinerario> itis = itinerariosDao.getItinerarios();
    		Random r = new Random();
    		estudiante.setItinerario(itis.get(r.nextInt(itis.size())).getTipo());
    		estudiante.setSemestre_inicio_instancia(r.nextInt(2)+1);
    		estudianteDao.addEstudiante(estudiante);
    	}
    }

    @RequestMapping("/") 
    public String index(Model model) {
        return "redirect:" +raiz + "/index.html";
    }
    
    @RequestMapping("/ofertes") 
    public String listOfertas(Model model, HttpSession session) { 
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("alumno") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	Estudiante estudiante = estudianteDao.getEstudiante(user.getAlias());
    	List<Oferta_de_proyecto> ofs = ofertaDao.getOfertas_de_proyectoItinerario(estudiante.getItinerario());
        List<Oferta_de_proyecto> preferencias = oferta_listaDao.getOfertas_desde_Itinerario(estudiante.getItinerario(), estudiante.getAlias());
        int count = preferencias.size();
    	for(int i = 0; i < ofs.size() && 0 < count; i++) {
    		for( Oferta_de_proyecto oferta : preferencias) {
    			if(ofs.get(i).getId() == oferta.getId()) {
    				ofs.remove(i);
    				count--;
    				break;
    			}
    		} 
    	}
        ofs.removeAll(preferencias);
        model.addAttribute("ofertas", ofs);
        model.addAttribute("preferencias", preferencias);
        return raiz + "/listar_ofertas";
    }
    
    @RequestMapping(value="/oferta/{id}", method = RequestMethod.GET) 
    public String viewEstudiante(Model model, @PathVariable int id, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("alumno") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	model.addAttribute("prioririza",puedePriorizar(user.getAlias()));
    	
    	Oferta_de_proyecto oferta;
    	try{
    		oferta = oferta_listaDao.getOferta(id, user.getAlias());
    	}
    	catch(EmptyResultDataAccessException e){
    		oferta = ofertaDao.getOferta_de_proyecto(id);
    		oferta.setPrioridad(1);
    	}
        model.addAttribute("oferta", oferta);
        return raiz + "/veure_oferta"; 
    }
    
    @RequestMapping(value="/esborrar/{id}", method = RequestMethod.GET) 
    public String deletePrioriti(Model model, @PathVariable int id, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("alumno") && !user.getRol().equals("groot")))
    		return "/no_acces";

    	List<Preferencia_alumno> list = preferenciaDao.get_Preferencias_alumno(user.getAlias());
		Preferencia_alumno borrar = preferenciaDao.get_Preferencia_alumno(user.getAlias(), id);
    	for(int i = borrar.getPrioridad()-1; i < list.size(); i++) {
			Preferencia_alumno cambio = preferenciaDao.get_Preferencia_alumno(user.getAlias(), list.get(i).getOferta());
			if (cambio.getPrioridad()!=1)
				cambio.setPrioridad(cambio.getPrioridad()-1);
			preferenciaDao.updatePreferencia_alumno(cambio);
		}
    	preferenciaDao.deletePreferencia_alumno(user.getAlias(), id);
        return "redirect:" + raiz + "/ofertes.html"; 
    }
    
    @RequestMapping(value="/oferta/{id}", method = RequestMethod.POST) 
    public String viewEstudiantePreferencia(Model model,
    		@ModelAttribute("oferta") Oferta_de_proyecto oferta,
    		@PathVariable int id, HttpSession session, BindingResult bindingResult) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("alumno") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	oferta.includo(ofertaDao.getOferta_de_proyecto(id));
    	EstudianteValidator estudianteValidator = new EstudianteValidator();
		estudianteValidator.validate(oferta, bindingResult);
        if (bindingResult.hasErrors())
    		return raiz + "/veure_oferta"; 
    	setPriority(oferta, user.getAlias());
    	return "redirect:" + raiz + "/ofertes.html"; 
    }
   
    
    private void setPriority(Oferta_de_proyecto oferta, String alias) {
    	List<Preferencia_alumno> list = preferenciaDao.get_Preferencias_alumno(alias);
    	Preferencia_alumno solictada = new Preferencia_alumno();
    	solictada.setAlumno(alias);
    	solictada.setEstado("Oberta");
    	solictada.setOferta(oferta.getId());
    	solictada.setPrioridad(oferta.getPrioridad());
    	try {
    		Preferencia_alumno antigua = preferenciaDao.get_Preferencia_alumno(alias, oferta.getId());
    		if(antigua.getPrioridad()!=oferta.getPrioridad()) {
    			if(antigua.getPrioridad()>solictada.getPrioridad()) {
	        		for(int i = solictada.getPrioridad()-1; i < antigua.getPrioridad() &&  i < list.size(); i++) {
	        			Preferencia_alumno cambio = preferenciaDao.get_Preferencia_alumno(alias, list.get(i).getOferta());
	        			cambio.setPrioridad(cambio.getPrioridad()+1);
	        			preferenciaDao.updatePreferencia_alumno(cambio);
	        		}
    			}
    			if(antigua.getPrioridad()<solictada.getPrioridad()) {
	        		for(int i=antigua.getPrioridad()-1; i < solictada.getPrioridad() && i < list.size(); i++) {
	        			Preferencia_alumno cambio = preferenciaDao.get_Preferencia_alumno(alias, list.get(i).getOferta());
	        			if (cambio.getPrioridad()!=1)
	        				cambio.setPrioridad(cambio.getPrioridad()-1);
	        			preferenciaDao.updatePreferencia_alumno(cambio);
	        		}
    	    		if(solictada.getPrioridad()>list.size())
    	    			solictada.setPrioridad(list.size());
    			}
    			
    			preferenciaDao.updatePreferencia_alumno(solictada);
    		}
    		
    	}catch(EmptyResultDataAccessException e) {
    		if(solictada.getPrioridad()<=list.size())
	    		for(int i=solictada.getPrioridad()-1; i < list.size(); i++) {
	    			Preferencia_alumno cambio = preferenciaDao.get_Preferencia_alumno(alias, list.get(i).getOferta());
	    			cambio.setPrioridad(cambio.getPrioridad()+1);
	    			preferenciaDao.updatePreferencia_alumno(cambio);
	    		}
    		else
    			solictada.setPrioridad(list.size()+1);
    			
    		preferenciaDao.addPreferencia_alumno(solictada);
    	}
    	for(int i = 0; i < list.size(); i++)
    		System.out.println("\t\tOferta:" + list.get(i).getOferta() + "\tpreferencia:" + list.get(i).getPrioridad());

    }
    
    @RequestMapping("/index") 
    public String viewAsignacio(Model model, HttpSession session) { 
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("alumno") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	Estudiante est = estudianteDao.getEstudiante(user.getAlias());
    	if( 0 == est.getSemestre_inicio_instancia()) {
    		model.addAttribute("semestre",est);
    		return raiz + "/seleccio_semestre";
    	}
    	
    	Asigna asig = null;
    	try {
    		asig = asignaDao.getAsignaPendiente(user.getAlias());
    		Oferta_de_proyecto ofer = oferta_listaDao.getOferta(asig.getProyecto(), user.getAlias());
    		model.addAttribute("oferta", ofer);
    		session.setAttribute("asignacio", asig);
    	}catch(EmptyResultDataAccessException e)
    	{
        	try {
        		asig = asignaDao.getAsignaAceptada(user.getAlias());
        		Oferta_de_proyecto ofer = oferta_listaDao.getOferta(asig.getProyecto(), user.getAlias());
        		model.addAttribute("oferta", ofer);
        		session.setAttribute("asignacio", asig);
        	}catch(EmptyResultDataAccessException ex)
        	{
            	try {
            		asig = asignaDao.getAsignaRechazada(user.getAlias());
            		Oferta_de_proyecto ofer = oferta_listaDao.getOferta(asig.getProyecto(), user.getAlias());
            		model.addAttribute("oferta", ofer);
            		session.setAttribute("asignacio", asig);
            	}catch(NullPointerException exy)
            	{
            	}
        	}
    	}
        model.addAttribute("asignacio",asig);
        session.setAttribute("asignacion",asig);
        return raiz + "/inicio_asignacion";
    }
    
    @RequestMapping(value="/index", method = RequestMethod.POST) 
    public String processUpdateSubmit(
                            @ModelAttribute("asignacio") Asigna asig_comment, 
                            BindingResult bindingResult, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("alumno") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	Asigna asig = (Asigna) session.getAttribute("asignacion");
    	Oferta_de_proyecto oferta = ofertaDao.getOferta_de_proyecto(asig.getProyecto());
    	if(oferta == null || asig == null)
    		return "/error";
        if (bindingResult.hasErrors()) 
            return raiz +"/inicio_asignacion";
    	oferta.setEstado("Visible per alumnes");
        ofertaDao.updateOferta_de_proyecto(oferta);
        asignaDao.rejectAsigna(asig_comment.getComentario_peticion_cambio(), asig.getProyecto(), asig.getAlumno());
        session.removeAttribute("asignacion");
        return "redirect:/"; 
      }
    
    @RequestMapping("/aceptar")
    public String acceptAsignacio(Model model, HttpSession session) { 
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("alumno") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	Asigna asig = (Asigna) session.getAttribute("asignacion");
    	if(asig == null)
    		return "/error";
    	asignaDao.acceptAsigna(asig.getProyecto(), asig.getAlumno());
        session.removeAttribute("asignacion");
        return "redirect:/";
    }
    
    @RequestMapping(value="/eleccio_semestre", method = RequestMethod.POST) 
    public String eleccioSemestre(@ModelAttribute("semestre") Estudiante estud, Model model, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("alumno") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	
    	Estudiante est = estudianteDao.getEstudiante(user.getAlias());
    	est.setSemestre_inicio_instancia(estud.getSemestre_inicio_instancia());

        model.addAttribute("semestre",est.getSemestre_inicio_instancia());
        session.setAttribute("estudiante",est);
        return raiz + "/confirmacio_semestre";
    }
    
    @RequestMapping("/confirmacio_semestre") 
    public String confirmacioSemestre(Model model, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("alumno") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	
    	Estudiante est = (Estudiante) session.getAttribute("estudiante");
    	if(est == null)
    		return "/error";
    	estudianteDao.updateEstudiante(est);
        session.removeAttribute("estudiante");

    	return "redirect:/";
    }
    
}
