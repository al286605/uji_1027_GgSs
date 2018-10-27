package es.uji.ei1027.GgSs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.GgSs.modelo.Preferencia_alumno;

@Repository
public class Preferencia_alumnoDao {
	private JdbcTemplate jdbcTemplate;
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}
	private static final class ContactoMapper implements RowMapper<Preferencia_alumno> { 

	    public Preferencia_alumno mapRow(ResultSet rs, int rowNum) throws SQLException { 
	        Preferencia_alumno preferencia = new Preferencia_alumno();
	        preferencia.setAlumno(rs.getString("alumno"));
	        preferencia.setOferta(rs.getInt("id_oferta"));
	        preferencia.setPrioridad(rs.getInt("prioridad"));
	        preferencia.setEstado(rs.getString("estado"));
	        
	        return preferencia;
	    }
	}

	public List<Preferencia_alumno> get_PersonasDeContacto() {
		return this.jdbcTemplate.query(
		     	"select * from Preferencia", new ContactoMapper());
	}
	
	public List<Preferencia_alumno> get_Preferencias_alumno(String alumno) {
		return this.jdbcTemplate.query(
				"select * from Preferencia where alumno =? ORDER BY prioridad",  
				new Object[] {alumno}, new ContactoMapper());
	}
		
	public Preferencia_alumno get_Preferencia_alumno(String alumno, int oferta) {
		return this.jdbcTemplate.queryForObject(
				"select * from Preferencia where alumno =? and id_oferta=? ORDER BY prioridad",  
				new Object[] {alumno,oferta}, new ContactoMapper());
	}
	
	public void addPreferencia_alumno(Preferencia_alumno preferencia) {
		this.jdbcTemplate.update(
				"insert into Preferencia( alumno, id_oferta, prioridad, estado) values(?, ?, ?, ?)", 
				preferencia.getAlumno(), preferencia.getOferta(), preferencia.getPrioridad(), preferencia.getEstado());
	}
		
	public void updatePreferencia_alumno(Preferencia_alumno preferencia) {
		this.jdbcTemplate.update(
				"update Preferencia set prioridad=?, estado=? where alumno=? and id_oferta=?",
				 preferencia.getPrioridad(), preferencia.getEstado(), preferencia.getAlumno(), preferencia.getOferta() );
	}
		
	public void deletePreferencia_alumno(String alumno, int oferta) {
		this.jdbcTemplate.update(
		        "delete from Preferencia where alumno = ? and id_oferta = ?", alumno, oferta);
	}
}
