package es.uji.ei1027.GgSs.controller;

import org.springframework.validation.Errors;

import es.uji.ei1027.GgSs.modelo.Oferta_de_proyecto;

public class DccValidator {
	
	public boolean supports(Class<?> cls){
		return Oferta_de_proyecto.class.equals(cls);
	}
	
	public void validate(Object obj, Errors errors){
		Oferta_de_proyecto oferta = (Oferta_de_proyecto) obj;
		if (oferta.getTexto_revision().trim().equals(""))
			errors.rejectValue("texto_revision", "obligatoria", "Introduix una retroacció");
	}
	
}
