package es.uji.ei1027.GgSs.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.uji.ei1027.GgSs.dao.UsuarioDao;
import es.uji.ei1027.GgSs.modelo.Usuario;

@Controller
public class AccessController {
	private String raiz = "";
	
	private UsuarioDao usuario_dao;
	
    @Autowired
    public void setAsignacionDao(UsuarioDao dao) { 
        this.usuario_dao=dao;
    }
    @RequestMapping("") 
    public String none(Model model) {
        return "redirect:" +raiz + "/";
    }   

    @RequestMapping("/") 
    public String root(Model model) {
        return "redirect:" +raiz + "/acces.html";
    }

   @RequestMapping("/index") 
    public String index(Model model) {
        return "redirect:" +raiz;
    }


    @RequestMapping("/acces") 
    public String mostraAcces(Model model, HttpSession session) {
    	//usuario_dao.addRootUser();
    	Usuario user = (Usuario) session.getAttribute("usuario");
    	if(user == null) {    		
    		model.addAttribute("usuari", new Usuario());
    		return raiz + "/acces";
    	}
    	
    	switch(user.getRol())
    	{
    		case "empresa":
    			return "redirect:/empresa/";
    		case "btc":
    			return "redirect:/btc/";
    		case "dcc":
    			return "redirect:/dcc/";
    		case "alumno":
    			return "redirect:/alumne/";
    		case "groot":
    			return "redirect:/admin.html";
    		default:
    			return "redirect:/eixida.html";
    	}
    }
    
    @RequestMapping(value="/acces", method=RequestMethod.POST) 
    public String identificacioUsuari(@ModelAttribute("usuari") Usuario usuari,
                                    BindingResult bindingResult, HttpSession session) {
    	Usuario user = usuario_dao.autenticarUsuario(usuari);
    	if(user == null)
    	{	

    		bindingResult.rejectValue("alias", "badpw", "Correu o contrasenya errada");
        	return raiz + "/acces";
    	}
    	session.setAttribute("usuario", user);
    	return "redirect:/";
    }
    
    @RequestMapping(value="/eixida")
    public String eixida(HttpSession session)    {
    	session.invalidate();
    	return "redirect:/";
    }
    
    
}
