package es.uji.ei1027.GgSs.modelo;

public class PersonaDeContacto {
	private String alias;
	private String nombre;
	private String empresa;
	private String puesto;
	public PersonaDeContacto() {}
	public PersonaDeContacto(UserContacto user) {
		alias = user.getAlias();
		nombre = user.getNombre();
		puesto = user.getPuesto();
	}
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getPuesto() {
		return puesto;
	}
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	
}
