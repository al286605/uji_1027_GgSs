package es.uji.ei1027.GgSs.modelo;

public class Empresa {
	private String cif;
	private String nombre;
	private String direccion;
	private String telefono;
	private String cif_old;
	public String getCif_old() {
		return cif_old;
	}
	public void setCif_old(String cif_old) {
		this.cif_old = cif_old;
	}
	public String getCif() {
		return cif;
	}
	public void setCif(String cif) {
		this.cif = cif;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
}
