package es.uji.ei1027.GgSs.modelo;

import java.util.Date;

public class Asigna {
	private int proyecto;
	private String alumno;

	private String profesor;
	private String estado;
	private Date fecha_propuesta;
	private Date fecha_aceptacion_o_rechazo;
	private Date fecha_traspaso_iglu;
	private Date fecha_peticion_cambio;
	private String comentario_peticion_cambio;
	
	public int getProyecto() {
		return proyecto;
	}
	public void setProyecto(int proyecto) {
		this.proyecto = proyecto;
	}
	public String getAlumno() {
		return alumno;
	}
	public void setAlumno(String alumno) {
		this.alumno = alumno;
	}
	public String getProfesor() {
		return profesor;
	}
	public void setProfesor(String profesor) {
		this.profesor = profesor;
	}
	public Date getFecha_propuesta() {
		return fecha_propuesta;
	}
	public void setFecha_propuesta(Date fecha_propuesta) {
		this.fecha_propuesta = fecha_propuesta;
	}

	public Date getFecha_aceptacion_o_rechazo() {
		return fecha_aceptacion_o_rechazo;
	}
	public void setFecha_aceptacion_o_rechazo(Date fecha_aceptacion_o_rechazo) {
		this.fecha_aceptacion_o_rechazo = fecha_aceptacion_o_rechazo;
	}
	public Date getFecha_traspaso_iglu() {
		return fecha_traspaso_iglu;
	}
	public void setFecha_traspaso_iglu(Date fecha_traspaso_iglu) {
		this.fecha_traspaso_iglu = fecha_traspaso_iglu;
	}
	public String getComentario_peticion_cambio() {
		return comentario_peticion_cambio;
	}
	public void setComentario_peticion_cambio(String comentario_peticion_cambio) {
		this.comentario_peticion_cambio = comentario_peticion_cambio;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Date getFecha_peticion_cambio() {
		return fecha_peticion_cambio;
	}
	public void setFecha_peticion_cambio(Date fecha_peticion_cambio) {
		this.fecha_peticion_cambio = fecha_peticion_cambio;
	}


}
