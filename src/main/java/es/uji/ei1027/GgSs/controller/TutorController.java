package es.uji.ei1027.GgSs.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.uji.ei1027.GgSs.dao.TutorDao;
import es.uji.ei1027.GgSs.modelo.Tutor;


@Controller
@RequestMapping("/tutor")
public class TutorController {
	private String raiz = "tutor";
	
	private TutorDao tutorDao; 

    @Autowired
    public void setEstudianteDao(TutorDao tutorDao) { 
        this.tutorDao=tutorDao;
    }

    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    
    @RequestMapping("/list") 
    public String listTutores(Model model) {
        model.addAttribute("tutores", tutorDao.getTutores());
        return raiz + "/list";
    }

    @RequestMapping("/add") 
    public String addTutores(Model model) {
        model.addAttribute("tutor", new Tutor());
        return raiz + "/add";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST) 
    public String processAddSubmit(@ModelAttribute("tutor") Tutor tutor,
                                    BindingResult bindingResult) {  
         if (bindingResult.hasErrors()) 
                return raiz + "/add";
         tutorDao.addTutor(tutor);
         return "redirect:list"; 
     }
    
    @RequestMapping(value="/update/{nom}", method = RequestMethod.GET) 
    public String editTutor(Model model, @PathVariable String nom) { 
        model.addAttribute("tutor", tutorDao.getTutor(nom));
        return raiz + "/update"; 
    }
    
    @RequestMapping(value="/update/{nom}", method = RequestMethod.POST) 
    public String processUpdateSubmit(@PathVariable String nom, 
                            @ModelAttribute("tutor") Tutor tutor, 
                            BindingResult bindingResult) {
         if (bindingResult.hasErrors()) 
             return raiz +"/update";
         tutorDao.updateTutor(tutor);
         return "redirect:../list"; 
      }

    @RequestMapping(value="/delete/{nom}")
    public String processDelete(@PathVariable String nom) {
           tutorDao.deleteTutor(nom);
           return "redirect:../list"; 
    }
}
