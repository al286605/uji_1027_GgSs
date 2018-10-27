package es.uji.ei1027.GgSs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.GgSs.modelo.Estado;

@Repository
public class EstadoDao {
	private JdbcTemplate jdbcTemplate;
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}
	
	private static final class EstadoMapper implements RowMapper<Estado> { 

	    public Estado mapRow(ResultSet rs, int rowNum) throws SQLException { 
	        Estado estados_de_oferta = new Estado();
	        estados_de_oferta.setTipo(rs.getString("tipo"));
	        return estados_de_oferta;
	    }
	}
	
	public List<Estado> getOfertao() {
		return this.jdbcTemplate.query(
		     	"select * from estados_de_oferta;", new EstadoMapper());
	}	
	public Estado getOferta(String tipo) {
		return this.jdbcTemplate.queryForObject(
				"select * from estados_de_oferta where tipo=?",  
				new Object[] {tipo}, new EstadoMapper());
	}
	
	
	public List<Estado> getPreferencias() {
		return this.jdbcTemplate.query(
		     	"select * from Estados_preferencia_de_practicas;", new EstadoMapper());
	}	
	public Estado getPreferencia(String tipo) {
		return this.jdbcTemplate.queryForObject(
				"select * from Estados_preferencia_de_practicas where tipo=?",  
				new Object[] {tipo}, new EstadoMapper());
	}
	

	
	public List<Estado> getAsignaciones() {
		return this.jdbcTemplate.query(
		     	"select * from estados_de_asignacion;", new EstadoMapper());
	}
		
	public Estado getAsignacion(String tipo) {
		return this.jdbcTemplate.queryForObject(
				"select * from estados_de_asignacion where tipo=?",  
				new Object[] {tipo}, new EstadoMapper());
	}
}
