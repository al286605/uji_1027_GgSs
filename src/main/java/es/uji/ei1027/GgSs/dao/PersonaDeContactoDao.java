package es.uji.ei1027.GgSs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.GgSs.modelo.PersonaDeContacto;

@Repository
public class PersonaDeContactoDao {
	private JdbcTemplate jdbcTemplate;
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}
	private static final class ContactoMapper implements RowMapper<PersonaDeContacto> { 

	    public PersonaDeContacto mapRow(ResultSet rs, int rowNum) throws SQLException { 
	        PersonaDeContacto contacto = new PersonaDeContacto();
	        contacto.setAlias(rs.getString("alias"));
	        contacto.setNombre(rs.getString("nombre"));
	        contacto.setEmpresa(rs.getString("empresa"));
	        contacto.setPuesto(rs.getString("puesto"));
	        
	        return contacto;
	    }
	}

	public List<PersonaDeContacto> getPersonasDeContacto() {
		return this.jdbcTemplate.query(
		     	"select * from Persona_de_contacto", new ContactoMapper());
	}
		
	public PersonaDeContacto getPersonaDeContacto(String alias) {
		return this.jdbcTemplate.queryForObject(
				"select * from Persona_de_contacto where alias=?",  
				new Object[] {alias}, new ContactoMapper());
	}
	
	public void addPersonaDeContacto(PersonaDeContacto contacto) {
		this.jdbcTemplate.update(
				"insert into Persona_De_Contacto(alias, nombre, empresa, puesto) values(?, ?, ?, ?)", 
				contacto.getAlias(), contacto.getNombre(), contacto.getEmpresa(), contacto.getPuesto());
	}
		
	public void updatePersonaDeContacto(PersonaDeContacto contacto) {
		this.jdbcTemplate.update(
				"update Persona_De_Contacto set nombre=?, empresa=?, puesto=? where alias = ?", 
				contacto.getNombre(), contacto.getEmpresa(), contacto.getPuesto(), contacto.getAlias());
	}
		
	public void deletePersonaDeContacto(String alias) {
		this.jdbcTemplate.update(
		        "delete from Persona_de_contacto where alias = ?", alias);
	}
}
