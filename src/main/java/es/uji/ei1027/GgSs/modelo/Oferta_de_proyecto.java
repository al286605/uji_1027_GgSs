package es.uji.ei1027.GgSs.modelo;

import java.sql.Date;
import java.util.List;

public class Oferta_de_proyecto {
	private int id;
	private String titulo;
	private String descripcion;
	private String persona_de_contacto;
	private String alias_persona_de_contacto;
	private String estado;
	private Date fecha_alta;
	private Date fecha_ultimo_cambio;
	private List<String> itinerario;
	private int pago = -1;
	private Date fecha_revision;
	private String texto_revision;
	
	private int prioridad;
	private String empresa;
	
	public void includo(Oferta_de_proyecto other) {
		if (titulo == null)
			titulo = other.titulo;
		if (descripcion == null)
			descripcion = other.descripcion;
		if (persona_de_contacto == null)
			persona_de_contacto = other.persona_de_contacto;
		if (estado == null)
			estado = other.estado;
		if (fecha_alta == null)
			fecha_alta = other.fecha_alta;
		if (fecha_ultimo_cambio == null)
			fecha_ultimo_cambio = other.fecha_ultimo_cambio;
		if (itinerario == null)
			itinerario = other.itinerario;
		if (fecha_revision == null)
			fecha_revision = other.fecha_revision;
		if (texto_revision == null)
			texto_revision = other.texto_revision;
		if (empresa == null)
			empresa = other.empresa;
		if (alias_persona_de_contacto == null)
			alias_persona_de_contacto = other.alias_persona_de_contacto;
		if (prioridad <= 0)
			prioridad = other.prioridad;
		if (id <= 0)
			id = other.id;
		if (pago < 0)
			pago = other.pago;
		
	}
	
	public boolean isEdit() { return estado.equals("Pendent de revisió") || estado.equalsIgnoreCase("Rebutjada");}
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getPersona_de_contacto() {
		return persona_de_contacto;
	}
	public void setPersona_de_contacto(String persona_de_contacto) {
		this.persona_de_contacto = persona_de_contacto;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Date getFecha_alta() {
		return fecha_alta;
	}
	public void setFecha_alta(Date fecha_alta) {
		this.fecha_alta = fecha_alta;
	}
	public Date getFecha_ultimo_cambio() {
		return fecha_ultimo_cambio;
	}
	public void setFecha_ultimo_cambio(Date fecha_ultimo_cambio) {
		this.fecha_ultimo_cambio = fecha_ultimo_cambio;
	}
	public List<String> getItinerario() {
		return itinerario;
	}
	public void setItinerario(List<String> itinerario) {
		this.itinerario = itinerario;
	}
	public int getPago() {
		return pago;
	}
	public void setPago(int pago) {
		this.pago = pago;
	}
	public Date getFecha_revision() {
		return fecha_revision;
	}
	public void setFecha_revision(Date fecha_revision) {
		this.fecha_revision = fecha_revision;
	}
	public String getTexto_revision() {
		return texto_revision;
	}
	public void setTexto_revision(String texto_revision) {
		this.texto_revision = texto_revision;
	}
	public int getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getAlias_persona_de_contacto() {
		return alias_persona_de_contacto;
	}

	public void setAlias_persona_de_contacto(String alias_persona_de_contacto) {
		this.alias_persona_de_contacto = alias_persona_de_contacto;
	}
	
}
