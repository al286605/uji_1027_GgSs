package es.uji.ei1027.GgSs.controller;

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

import es.uji.ei1027.GgSs.dao.EmpresaDao;
import es.uji.ei1027.GgSs.dao.UsuarioDao;
import es.uji.ei1027.GgSs.dao.PersonaDeContactoDao;
import es.uji.ei1027.GgSs.modelo.Empresa;
import es.uji.ei1027.GgSs.modelo.PersonaDeContacto;
import es.uji.ei1027.GgSs.modelo.UserContacto;
import es.uji.ei1027.GgSs.modelo.Usuario;

@Controller
@RequestMapping("/registre_empresa")
public class RegistrarEmpresaController {
	
	private String raiz = "/registre_empresa";
	
	private EmpresaDao empresaDao;
	private UsuarioDao usuarioDao;
	private PersonaDeContactoDao personaDao;

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

    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    
    @RequestMapping(value="/trobar_empresa?{nombre}", method = RequestMethod.GET) 
    public String listEmpresa(Model model, @PathVariable String nombre) {
        model.addAttribute("empresas", empresaDao.getEmpresas(nombre));
        return raiz + "/trobar_empresa";
    }
    
    @RequestMapping("") 
    public String none(Model model) {
        return "redirect:" +raiz + "/index.html";
    }
    
    @RequestMapping("/") 
    public String index(Model model) {
        return "redirect:" +raiz + "/index.html";
    }

    @RequestMapping("/index") 
    public String addEmpresa(Model model) {
        model.addAttribute("empresa", new Empresa());
        return raiz + "/index";
    }

    @RequestMapping(value="/index", method=RequestMethod.POST) 
    public String processAddSubmit(@ModelAttribute("empresa") Empresa empresa,
                                    BindingResult bindingResult) {

         if (bindingResult.hasErrors()) 
                return raiz + "/index";
         try {
        	 Empresa comprobacion = empresaDao.getEmpresa(empresa.getCif());
        	 //empresa.setCif_old(comprobacion.getCif());
        	 //empresaDao.updateEmpresa(empresa);        	 
         }catch(EmptyResultDataAccessException e){
        	 empresaDao.addEmpresa(empresa);
         }
         return "redirect:" + raiz + "/contacte_empresa/"+empresa.getCif(); 
     }
    
    @RequestMapping(value="/empresa_trobada.html", method = RequestMethod.GET)
    public String registrarContacteDesdeFormEmpresa(Model model, @ModelAttribute("empresa") Empresa empr, HttpSession session)
    {
        return "redirect:"+raiz+"/empresa_trobada/"+empr.getCif(); 
    }
    
    @RequestMapping(value="/empresa_trobada/{cif}", method = RequestMethod.GET)
    public String registrarContacteDesdeEmpresa(Model model, @PathVariable String cif, HttpSession session)
    {
    	Empresa empresa = empresaDao.getEmpresa(cif);
        session.setAttribute("empresa", empresa);
        return "redirect:"+raiz+"/contacte_empresa.html"; 
    }
    
    @RequestMapping(value="/contacte_empresa")
    public String registrarContacteDeEmpresa(Model model, HttpSession session)
    {
    	Empresa empresa = (Empresa) session.getAttribute("empresa");
    	if(empresa==null)
    		return raiz + "/no_access";
        model.addAttribute("empresa", empresa);
        model.addAttribute("user", new UserContacto());
		return raiz + "/contacte_empresa";
    }

    @RequestMapping(value="/contacte_empresa", method = RequestMethod.POST)
    public String procesoDeRegistroContacteDeEmpresa(
            @ModelAttribute("user") UserContacto user,
            @ModelAttribute("empresa") Empresa empresa,
            BindingResult bindingResult, HttpSession session)
    {
    	empresa = (Empresa) session.getAttribute("empresa");
    	if(empresa==null)
    		return raiz + "/no_access";
    	Usuario usuario = new Usuario(user);
    	PersonaDeContacto persona = new PersonaDeContacto(user);
    	persona.setEmpresa(empresa.getCif());
    	
    	if (bindingResult.hasErrors())
    		return raiz + "/contacte_empresa";
    	
    	usuarioDao.addUsuario(usuario);
    	personaDao.addPersonaDeContacto(persona);
    	return raiz + "/contacte_empresa_afegit";
    }
}