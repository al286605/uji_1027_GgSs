package es.uji.ei1027.GgSs.controller;

import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.uji.ei1027.GgSs.dao.Oferta_de_proyectoDao;
import es.uji.ei1027.GgSs.dao.RevisionesDao;
import es.uji.ei1027.GgSs.modelo.Oferta_de_proyecto;
import es.uji.ei1027.GgSs.modelo.Revision;
import es.uji.ei1027.GgSs.modelo.Usuario;



@Controller
@RequestMapping("/dcc")
public class DccController {
	private String raiz = "/dcc";
	private Oferta_de_proyectoDao ofertaDao;
	private RevisionesDao revisionDao;

	@Autowired
    public void setRevisionDao(RevisionesDao dao) { 
        this.revisionDao=dao;
    }
    @Autowired
    public void setOferta_de_proyectoDao(Oferta_de_proyectoDao dao) { 
        this.ofertaDao=dao;
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
    	if(user == null || (!user.getRol().equals("dcc") && !user.getRol().equals("btc") && !user.getRol().equals("groot")))
    		return "/no_acces";
    	model.addAttribute("rol", user.getRol());
    	
        model.addAttribute("solicituts", ofertaDao.getOfertas_de_proyectoPendientesDeRevisar());
        return raiz + "/listar_ofertas";
    }
    
    @RequestMapping(value="/proyecte/{id}", method = RequestMethod.GET) 
    public String viewEstudiante(Model model, @PathVariable int id, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
		if(user == null || (!user.getRol().equals("dcc") && !user.getRol().equals("btc") && !user.getRol().equals("groot")))
			return "/no_acces";
		model.addAttribute("rol", user.getRol());
		
    	Oferta_de_proyecto oferta = ofertaDao.getOferta_de_proyecto(id);
        model.addAttribute("oferta", oferta);
        session.setAttribute("oferta", oferta);
        return raiz + "/veure_oferta"; 
    }
    
    @RequestMapping(value="/proyecte/{id}", method = RequestMethod.POST) 
    public String processRecjectSubmit(Model model, @PathVariable int id, 
							        @ModelAttribute("oferta") Oferta_de_proyecto oferta, 
							        BindingResult bindingResult, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
		if(user == null || (!user.getRol().equals("dcc") && !user.getRol().equals("btc") && !user.getRol().equals("groot")))
			return "/no_acces";
		model.addAttribute("rol", user.getRol());
		
		//Oferta_de_proyecto oferta = (Oferta_de_proyecto)session.getAttribute("oferta");
		oferta.includo((Oferta_de_proyecto)session.getAttribute("oferta"));
		if(oferta == null)
			return "/error";
		//oferta.setTexto_revision(revisar_oferta.getTexto_revision());
		System.out.println("\n\n\n\n\tOferta:" + oferta.getTitulo() + "\tEmpresa:" + oferta.getEmpresa());
		DccValidator dccValidator = new DccValidator();
		dccValidator.validate(oferta, bindingResult);
		if (bindingResult.hasErrors())  
             return raiz +"/veure_oferta";
		oferta.setFecha_revision( new java.sql.Date(new java.util.Date().getTime()));
        session.setAttribute("oferta", oferta);
        return "redirect:"+raiz + "/rebutx"; 
    }
    
    @RequestMapping(value="/rebutx", method = RequestMethod.GET) 
    public String processAskReject(Model model, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
		if(user == null || (!user.getRol().equals("dcc") && !user.getRol().equals("btc") && !user.getRol().equals("groot")))
			return "/no_acces";
		model.addAttribute("rol", user.getRol());
		
    	Oferta_de_proyecto oferta = (Oferta_de_proyecto)session.getAttribute("oferta");
    	if(oferta == null)
    		return "/error";
        model.addAttribute("oferta", oferta);
        return raiz + "/confirmar_rebutx"; 
    }
    
    @RequestMapping(value="/confirmar_rebutx", method = RequestMethod.GET) 
    public String processAcceptReject(Model model, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
		if(user == null || (!user.getRol().equals("dcc") && !user.getRol().equals("btc") && !user.getRol().equals("groot")))
			return "/no_acces";
		model.addAttribute("rol", user.getRol());
		
    	Oferta_de_proyecto oferta = (Oferta_de_proyecto)session.getAttribute("oferta");
    	if(oferta == null)
    		return "/error";

    	Revision revision = new Revision();
    	revision.setId(oferta.getId());
    	revision.setDescripcion(oferta.getDescripcion());
    	revision.setMensaje(oferta.getTexto_revision());
        revisionDao.addRevision(revision);
        
    	oferta.setEstado("Rebutjada");
        ofertaDao.updateOferta_de_proyecto(oferta);
        session.removeAttribute("oferta");
        return "redirect:"+raiz + "/"; 
    }
    
    @RequestMapping(value="/aceptar", method = RequestMethod.GET) 
    public String processAskAccept(Model model, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
		if(user == null || (!user.getRol().equals("dcc") && !user.getRol().equals("btc") && !user.getRol().equals("groot")))
			return "/no_acces";
		model.addAttribute("rol", user.getRol());
		
    	Oferta_de_proyecto oferta = (Oferta_de_proyecto)session.getAttribute("oferta");
    	if(oferta == null)
    		return "/error";
        model.addAttribute("oferta", oferta);
        return raiz + "/confirmar_aceptar"; 
    }
    
    @RequestMapping(value="/confirmar_aceptar", method = RequestMethod.GET) 
    public String processAcceptSubmit(Model model, HttpSession session) {
    	Usuario user = (Usuario)session.getAttribute("usuario");
		if(user == null || (!user.getRol().equals("dcc") && !user.getRol().equals("btc") && !user.getRol().equals("groot")))
			return "/no_acces";
		model.addAttribute("rol", user.getRol());
		
    	Oferta_de_proyecto oferta = (Oferta_de_proyecto)session.getAttribute("oferta");
    	if(oferta == null)
    		return "/error";
    	oferta.setEstado("Acceptada");
        ofertaDao.updateOferta_de_proyecto(oferta);
        session.removeAttribute("oferta");
        return "redirect:"+raiz + "/"; 
    }
}
