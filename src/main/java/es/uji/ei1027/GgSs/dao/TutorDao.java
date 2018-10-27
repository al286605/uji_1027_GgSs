package es.uji.ei1027.GgSs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.GgSs.modelo.Tutor;

@Repository
public class TutorDao {

	private JdbcTemplate jdbcTemplate;
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}
	
	
	private static final class TutorMapper implements RowMapper<Tutor> { 

	    public Tutor mapRow(ResultSet rs, int rowNum) throws SQLException { 
	        Tutor tutor = new Tutor();
	        tutor.setNombre(rs.getString("nombre"));
	        tutor.setAlias(rs.getString("alias"));
	        tutor.setDespacho(rs.getString("despacho"));
	        tutor.setDepartamento(rs.getString("departamento"));
	        
	        return tutor;
	    }
	}
	
	public List<Tutor> getTutores() {
		return this.jdbcTemplate.query(
		     	"select alias, nombre, departamento, despacho from tutor;", new TutorMapper());
	}
		
	public Tutor getTutor(String alias) {
		return this.jdbcTemplate.queryForObject(
				"select alias, nombre, departamento, despacho from tutor where alias=?",  
				new Object[] {alias}, new TutorMapper());
	}
	
	public void addTutor(Tutor tutor) {
		this.jdbcTemplate.update(
				"insert into Tutor(alias, nombre, departamento, despacho) values(?, ?, ?, ?)", 
				tutor.getAlias(), tutor.getNombre(), tutor.getDepartamento(), tutor.getDespacho());
	}
		
	public void updateTutor(Tutor tutor) {
		this.jdbcTemplate.update(
				"update Tutor set nombre=?, departamento=?, despacho=? where alias=?", 
				tutor.getNombre(), tutor.getDepartamento(), tutor.getDespacho(),tutor.getAlias());
	}
		
	public void deleteTutor(String alias) {
		this.jdbcTemplate.update(
		        "delete from tutor where alias = ?", alias);
	}
}
