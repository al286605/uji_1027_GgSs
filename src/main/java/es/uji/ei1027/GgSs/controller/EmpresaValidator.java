package es.uji.ei1027.GgSs.controller;

import org.springframework.validation.Errors;

import es.uji.ei1027.GgSs.modelo.Oferta_de_proyecto;

public class EmpresaValidator {

	
	public boolean supports(Class<?> cls){
		return Oferta_de_proyecto.class.equals(cls);
	}
	
	public void validate(Object obj, Errors errors){
		Oferta_de_proyecto oferta = (Oferta_de_proyecto) obj;
		if (oferta.getTitulo().trim().equals(""))
			errors.rejectValue("titulo", "obligatoria", "introduce un titulo");
		if (oferta.getTitulo().trim().length()>100)
			errors.rejectValue("titulo", "Tamaño máximo", "Máximo 100 carácteres");
		
		if (oferta.getDescripcion().trim().equals(""))
			errors.rejectValue("descripcion", "obligatoria", "Debes añadir una descripcion breve");
		
		if (oferta.getPago() < 0)
			errors.rejectValue("pago", "obligatoria", "Hay que añadir el pago valido");
		
		if (oferta.getItinerario().isEmpty())
			errors.rejectValue("itinerario", "obligatoria", "Debes seleccionar al menos un itinerario");
	}
}
