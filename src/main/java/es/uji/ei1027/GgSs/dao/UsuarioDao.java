package es.uji.ei1027.GgSs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.GgSs.modelo.Usuario;

@Repository
public class UsuarioDao {
	 
	private JdbcTemplate jdbcTemplate;
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}

	private static final class UsuarioMapper implements RowMapper<Usuario> { 

	    public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException { 
	        Usuario usuario = new Usuario();
	        usuario.setAlias(rs.getString("alias"));
	        usuario.setCorreo(rs.getString("correo"));
	        usuario.setRol(rs.getString("rol"));
	        usuario.setContrasenya(rs.getString("contrasenya"));
	        
	        return usuario;
	    }
	}

	public List<Usuario> getUsuarios() {
		return this.jdbcTemplate.query(
		     	"select * from usuario;", new UsuarioMapper());
	}
		
	public Usuario getUsuario(String alias) {
		return this.jdbcTemplate.queryForObject(
				"select * from usuario where alias=?",  
				new Object[] {alias}, new UsuarioMapper());
	}
	
	public void addUsuario(Usuario usuario) {
		BasicPasswordEncryptor pass = new BasicPasswordEncryptor();
		usuario.setContrasenya(pass.encryptPassword(usuario.getContrasenya()));
		
		this.jdbcTemplate.update(
				"insert into Usuario(alias, correo, rol, contrasenya) values(?, ?, ?, ?)", 
				usuario.getAlias(), usuario.getCorreo(), 
				usuario.getRol(), usuario.getContrasenya());
	}
	
	public Usuario autenticarUsuario(Usuario usuario_introducido)
	{
		Usuario user_obtenido = null;
		try		{
			user_obtenido = this.jdbcTemplate.queryForObject(
					"select * from usuario where correo=?",  
					new Object[] {usuario_introducido.getCorreo()}, new UsuarioMapper());
			
		}catch(IncorrectResultSizeDataAccessException e){	}
		if(user_obtenido!=null)
		{
			BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor(); 
			if (passwordEncryptor.checkPassword(usuario_introducido.getContrasenya(), user_obtenido.getContrasenya()))
			{
				user_obtenido.setContrasenya(null);
				return user_obtenido;
			}			
		}

		return null;
	}
	
	public void addRootUser()
	{
		Usuario user = new Usuario();
		user = new Usuario();
		user.setAlias("garcia");
		user.setRol("dcc");
		user.setContrasenya("garcia");
		user.setCorreo("garcia@uji.es");
		addUsuario(user);
		/*user.setAlias("groot");
		user.setRol("groot");
		user.setContrasenya("groot");
		user.setCorreo("groot");
		addUsuario(user);
		user = new Usuario();
		user.setAlias("root");
		user.setRol("groot");
		user.setContrasenya("root");
		user.setCorreo("root");
		addUsuario(user);*/
	}
		
	public void updateUsuario(Usuario usuario) {
		this.jdbcTemplate.update(
				"update Usuario set correo=?, rol=?, contrasenya=? where alias = ?", 
				usuario.getCorreo(), usuario.getRol(), usuario.getContrasenya(), usuario.getAlias());
	}
		
	public void deleteUsuario(String alias) {
		this.jdbcTemplate.update(
		        "delete from usuario where alias = ?", alias);
	}
}
