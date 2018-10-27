package es.uji.ei1027.GgSs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.uji.ei1027.GgSs.dao.AsignacionDao;
import es.uji.ei1027.GgSs.modelo.Asigna;



@Controller
@RequestMapping("/asigna")
public class AsignaController {
	private String raiz = "asigna";
	
	private AsignacionDao asignaDao; 

    @Autowired
    public void setAsignacionDao(AsignacionDao asignaDao) { 
        this.asignaDao=asignaDao;
    }

    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    
    @RequestMapping("/list") 
    public String listAsignaciones(Model model) {
        model.addAttribute("asignaciones", asignaDao.getAsignaciones());
        return raiz + "/list";
    }

    @RequestMapping("/add") 
    public String addAsigna(Model model) {
        model.addAttribute("asigna", new Asigna());
        return raiz + "/add";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST) 
    public String processAddSubmit(@ModelAttribute("asigna") Asigna asignacion,
                                    BindingResult bindingResult) {  
         if (bindingResult.hasErrors()) 
                return raiz + "/add";
         asignaDao.addAsigna(asignacion);
         return "redirect:list"; 
     }
    
    /*@RequestMapping(value="/update/{nom}", method = RequestMethod.GET) 
    public String editEstudiante(Model model, @PathVariable String nom) { 
        model.addAttribute("asigna", asignaDao.getAsigna(nom));
        return raiz + "/update"; 
    }*/
    
    @RequestMapping(value="/update/{nom}", method = RequestMethod.POST) 
    public String processUpdateSubmit(@PathVariable String nom, 
                            @ModelAttribute("asigna") Asigna asignacion, 
                            BindingResult bindingResult) {
         if (bindingResult.hasErrors()) 
             return raiz +"/update";
         asignaDao.updateAsigna(asignacion);
         return "redirect:../list"; 
      }

    /*@RequestMapping(value="/delete/{nom}")
    public String processDelete(@PathVariable String nom) {
           asignaDao.deleteAsigna(nom);
           return "redirect:../list"; 
    }*/
}
