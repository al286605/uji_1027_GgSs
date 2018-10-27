package es.uji.ei1027.GgSs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.GgSs.modelo.Rol;

@Repository
public class RolDao {

	private JdbcTemplate jdbcTemplate;
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}
	
	private static final class RolMapper implements RowMapper<Rol> { 

	    public Rol mapRow(ResultSet rs, int rowNum) throws SQLException { 
	        Rol rol = new Rol();
	        rol.setNombre(rs.getString("nombre"));
	        return rol;
	    }
	}
	
	public List<Rol> getRoles() {
		return this.jdbcTemplate.query(
		     	"select * from roles;", new RolMapper());
	}
		
	public Rol getRol(String nombre) {
		return this.jdbcTemplate.queryForObject(
				"select * from roles where nombre=?",  
				new Object[] {nombre}, new RolMapper());
	}
}
