package es.uji.ei1027.GgSs.modelo;

public class Usuario {
	private String alias;
	private String correo;
	private String rol;
	private String contrasenya;
	public Usuario() {};
	public Usuario(UserContacto user) {
		alias = user.getAlias();
		correo = user.getCorreo();
		contrasenya = user.getContrasenya();
		rol = "empresa";
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias.toLowerCase();
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo.toLowerCase();
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getContrasenya() {
		return contrasenya;
	}
	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}
}
