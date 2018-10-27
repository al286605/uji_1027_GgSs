package es.uji.ei1027.GgSs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.GgSs.modelo.Empresa;

@Repository
public class EmpresaDao {

	private JdbcTemplate jdbcTemplate;
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}

	private static final class EmpresaMapper implements RowMapper<Empresa> { 

	    public Empresa mapRow(ResultSet rs, int rowNum) throws SQLException { 
	        Empresa empresa = new Empresa();
	        empresa.setCif(rs.getString("cif"));
	        empresa.setNombre(rs.getString("nombre"));
	        empresa.setTelefono(rs.getString("telefono"));
	        empresa.setDireccion(rs.getString("direccion"));
	        
	        return empresa;
	    }
	}

	public List<Empresa> getEmpresas() {
		return this.jdbcTemplate.query(
		     	"select * from empresa", new EmpresaMapper());
	}
	
	public List<Empresa> getEmpresas(String nombre) {
		String sql = "select * from empresa where nombre like ?";
		return this.jdbcTemplate.query(sql, new Object[] {nombre}, new EmpresaMapper());
	}
		
	public Empresa getEmpresa(String cif) {
		return this.jdbcTemplate.queryForObject(
				"select cif, nombre, telefono, direccion from empresa where cif=?",  
				new Object[] {cif}, new EmpresaMapper());
	}
	
	public void addEmpresa(Empresa empresa) {
		this.jdbcTemplate.update(
				"insert into Empresa(cif, nombre, telefono, direccion) values(?, ?, ?, ?)", 
				empresa.getCif(), empresa.getNombre(), empresa.getTelefono(), empresa.getDireccion());
	}
		
	public void updateEmpresa(Empresa empresa) {
		this.jdbcTemplate.update(
				"update Empresa set cif=?, nombre=?, telefono=?, direccion=? where cif = ?", 
				empresa.getCif(), empresa.getNombre(), empresa.getTelefono(), empresa.getDireccion(), empresa.getCif_old());
	}
		
	public void deleteEmpresa(String cif) {
		this.jdbcTemplate.update(
		        "delete from empresa where cif = ?", cif);
	}
	
}
