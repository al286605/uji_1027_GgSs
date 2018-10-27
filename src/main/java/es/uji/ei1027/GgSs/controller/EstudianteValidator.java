package es.uji.ei1027.GgSs.controller;

import org.springframework.validation.Errors;

import es.uji.ei1027.GgSs.modelo.Oferta_de_proyecto;

public class EstudianteValidator {

	
	public boolean supports(Class<?> cls){
		return Oferta_de_proyecto.class.equals(cls);
	}
	
	public void validate(Object obj, Errors errors){
		Oferta_de_proyecto oferta = (Oferta_de_proyecto) obj;
		if (oferta.getPrioridad() <= 0)
			errors.rejectValue("prioridad", "obligatoria", "Introduce una prioridad valida");
		if (oferta.getPrioridad() > 5)
			errors.rejectValue("prioridad", "obligatoria", "Introduce una prioridad valida");
	}
}
