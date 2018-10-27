package es.uji.ei1027.GgSs.modelo;

public class Estudiante {
	private String alias;
	private String dni;
	private String nombre;
	private String apellido;
	private String telefono;
	private int anyo_academico;
	private int numero_de_creditos_aprobados;
	private String itinerario;
	private int semestre_inicio_instancia;
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public int getAnyo_academico() {
		return anyo_academico;
	}


	public void setAnyo_academico(int anyo_academico) {
		this.anyo_academico = anyo_academico;
	}


	public int getNumero_de_creditos_aprobados() {
		return numero_de_creditos_aprobados;
	}


	public void setNumero_de_creditos_aprobados(int numero_de_creditos_aprobados) {
		this.numero_de_creditos_aprobados = numero_de_creditos_aprobados;
	}


	public String getItinerario() {
		return itinerario;
	}


	public void setItinerario(String itinerario) {
		this.itinerario = itinerario;
	}


	public int getSemestre_inicio_instancia() {
		return semestre_inicio_instancia;
	}


	public void setSemestre_inicio_instancia(int semestre_inicio_instancia) {
		this.semestre_inicio_instancia = semestre_inicio_instancia;
	}


	@Override
	public String toString(){
		return "Estudiante [dni="+dni+", nombre= " + nombre;
	}

}
	
	
	
	
	
	

