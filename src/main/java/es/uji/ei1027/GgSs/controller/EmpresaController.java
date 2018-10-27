package es.uji.ei1027.GgSs.controller;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.uji.ei1027.GgSs.dao.EmpresaDao;
import es.uji.ei1027.GgSs.dao.ItinerariosDao;
import es.uji.ei1027.GgSs.dao.Oferta_de_proyectoDao;
import es.uji.ei1027.GgSs.dao.PersonaDeContactoDao;
import es.uji.ei1027.GgSs.dao.RevisionesDao;
import es.uji.ei1027.GgSs.dao.UsuarioDao;
import es.uji.ei1027.GgSs.modelo.Oferta_de_proyecto;
import es.uji.ei1027.GgSs.modelo.PersonaDeContacto;
import es.uji.ei1027.GgSs.modelo.Revision;
import es.uji.ei1027.GgSs.modelo.Usuario;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {
	private String raiz = "/empresa";

	private RevisionesDao revisionDao;
	private EmpresaDao empresaDao;
	private UsuarioDao usuarioDao;
	private PersonaDeContactoDao personaDao;
	private Oferta_de_proyectoDao ofertaDao;
	private ItinerariosDao itinerariosDao;

	@Autowired
    public void setRevisionDao(RevisionesDao dao) { 
        this.revisionDao=dao;
    }
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
    

    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    
    

    @RequestMapping("") 
    public String none(Model model) {
        return "redirect:" +raiz + "/";
    }    

    @RequestMapping("/") 
    public String index(Model model) {
        return "redirect:" +raiz + "/index.html";
    }

    @RequestMapping("/index") 
    public String viewOfertas(Model model, HttpSession session) { 
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("empresa") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	String oferta_borrada = (String)session.getAttribute("oferta_borrada");
    	if (oferta_borrada != null)
    		session.removeAttribute("oferta_borrada");
    	model.addAttribute("borrada", oferta_borrada);
    	
    	String oferta_editada = (String)session.getAttribute("oferta_editada");
    	if (oferta_editada != null)
    		session.removeAttribute("oferta_editada");
    	model.addAttribute("editada", oferta_editada);
    	
    	PersonaDeContacto persona = personaDao.getPersonaDeContacto(user.getAlias());
        model.addAttribute("solicituts", ofertaDao.getOfertas_de_proyecto(persona.getEmpresa()));
        return raiz + "/index";
    }
    
    @RequestMapping("/crear_oferta") 
    public String addEmpresa(Model model) {
        model.addAttribute("oferta", new Oferta_de_proyecto());
        model.addAttribute("itinerarios", itinerariosDao.getItinerarios());
        return raiz + "/crear_oferta";
    }

    @RequestMapping(value="/crear_oferta", method=RequestMethod.POST) 
    public String processAddSubmit(Model model, @ModelAttribute("oferta") Oferta_de_proyecto oferta,
                                    BindingResult bindingResult, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("empresa") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	oferta.setPersona_de_contacto(user.getAlias());
    	oferta.setEstado("Pendent de revisió");
        model.addAttribute("itinerarios", itinerariosDao.getItinerarios());
		EmpresaValidator empresaValidator = new EmpresaValidator();
		empresaValidator.validate(oferta, bindingResult);
         if (bindingResult.hasErrors()) 
                return raiz + "/crear_oferta";
         ofertaDao.addOferta_de_proyecto(oferta);
         return "redirect:"+raiz; 
     }

    @RequestMapping(value="/edicio/{id}", method = RequestMethod.GET) 
    public String editEstudiante(Model model, @PathVariable int id) { 
        model.addAttribute("oferta", ofertaDao.getOferta_de_proyecto(id));
        model.addAttribute("itinerarios", itinerariosDao.getItinerarios());
        return raiz + "/editar_oferta"; 
    }
    
    @RequestMapping(value="/edicio/{id}", method = RequestMethod.POST) 
    public String processUpdateSubmit(Model model, @PathVariable int id, 
                            @ModelAttribute("oferta") Oferta_de_proyecto oferta, 
                            BindingResult bindingResult, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("empresa") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	oferta.setAlias_persona_de_contacto(user.getAlias());
    	oferta.setEstado("Pendent de revisió");
    	EmpresaValidator empresaValidator = new EmpresaValidator();
		empresaValidator.validate(oferta, bindingResult);
        model.addAttribute("itinerarios", itinerariosDao.getItinerarios());
         if (bindingResult.hasErrors()) 
             return raiz +"/editar_oferta";
         
         ofertaDao.updateOferta_de_proyecto(oferta);
     	 session.setAttribute("oferta_editada", oferta.getTitulo());
         return "redirect:"+raiz + "/"; 
    }
    
    @RequestMapping(value="/esborrar/{id}", method = RequestMethod.GET) 
    public String deleteEstudiante(Model model, HttpSession session, @PathVariable int id) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
    	if(user == null || (!user.getRol().equals("empresa") && !user.getRol().equals("groot")))
    		return "/no_acces";

    	session.setAttribute("oferta_borrada", ofertaDao.getOferta_de_proyecto(id).getTitulo());
    	ofertaDao.deleteOferta_de_proyecto(id);
        return "redirect:"+raiz + "/"; 
    }
    
    @RequestMapping(value="/proyecte/{id}", method = RequestMethod.GET) 
    public String viewEstudiante(Model model, @PathVariable int id) { 
    	Oferta_de_proyecto oferta = ofertaDao.getOferta_de_proyecto(id);
    	List<Revision> revisiones = revisionDao.getRevision(id);
    	int tmn = revisiones.size();
    	if ( tmn > 0) {
        	Revision revision = revisiones.get(tmn-1);
        	oferta.setFecha_revision(revision.getFecha());
        	oferta.setTexto_revision(revision.getMensaje());
    	}
    	System.out.println("Fecha:\t"+oferta.getFecha_revision() + "\tTexto revision:\t"+oferta.getTexto_revision());
        model.addAttribute("oferta", oferta);
        return raiz + "/veure_oferta"; 
    }
    
    @RequestMapping(value="/proyecte/{id}/historic", method = RequestMethod.GET) 
    public String viewHistoric(Model model, @PathVariable int id) { 
        model.addAttribute("oferta", ofertaDao.getOferta_de_proyecto(id));
        model.addAttribute("revisions", revisionDao.getRevision(id));
        return raiz + "/historico"; 
    }
}
