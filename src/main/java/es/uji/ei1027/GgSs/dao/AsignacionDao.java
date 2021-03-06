package es.uji.ei1027.GgSs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.GgSs.modelo.Asigna;

@Repository
public class AsignacionDao {

private JdbcTemplate jdbcTemplate;
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}
	//TODO dudo que esté acabado.
	private static final class AsignaMapper implements RowMapper<Asigna> { 

	    public Asigna mapRow(ResultSet rs, int rowNum) throws SQLException { 
	        Asigna asigna = new Asigna();
	        asigna.setProyecto(rs.getInt("id_oferta"));
	        asigna.setAlumno(rs.getString("alumno"));
	        try {
		        asigna.setId_alumno(rs.getString("id_alumno"));
	        }catch(PSQLException e) {}
	        
	        asigna.setProfesor(rs.getString("tutor"));
	        asigna.setEstado(rs.getString("estado"));
	        try {
	        asigna.setFecha_propuesta(rs.getDate("fecha_de_la_asignacion"));
	        asigna.setFecha_aceptacion_o_rechazo(rs.getDate("fecha_de_aceptacion"));
	        asigna.setFecha_traspaso_iglu(rs.getDate("fecha_de_traspaso_iglu"));
	        }catch(PSQLException e) {}
	        asigna.setComentario_peticion_cambio(rs.getString("comentario_peticion_de_cambio"));
	        try {
	        asigna.setFecha_peticion_cambio(rs.getDate("fecha_peticion_de_cambio"));

	        }catch(PSQLException e) {}
	        
	        return asigna;
	    }
	}
	
	public List<Asigna> getAsignaciones() {
		return this.jdbcTemplate.query(
		     	"select * from asignacion;", new AsignaMapper());
	}
	public List<Asigna> getAsignacionesRealizadas() {
		String sql = "select AL.alias as id_alumno, A.id_oferta, AL.nombre as alumno, O.titulo as comentario_peticion_de_cambio, T.nombre as tutor, A.estado "
     	+ " from Ofertas_de_practicas O, estudiante AL, tutor T, asignacion A "
     	+ " WHERE A.id_oferta=O.id AND A.alumno=AL.alias AND A.tutor=T.alias"
     	+ "  ORDER BY A.fecha_peticion_de_cambio";
		return this.jdbcTemplate.query(
				sql, new AsignaMapper());
	}
	
	public Asigna getAsignaPendiente(String nombre) {
		return this.jdbcTemplate.queryForObject(
				"select * from asignacion where alumno=? AND estado='Pendent'",  
				new Object[] {nombre}, new AsignaMapper());
	}

	public Asigna getAsignaAceptada(String nombre) {
		return this.jdbcTemplate.queryForObject(
				"select * from asignacion where alumno=? AND estado='Acceptada'",  
				new Object[] {nombre}, new AsignaMapper());
	}

	public Asigna getAsignaTraspasada(String nombre) {
		return this.jdbcTemplate.queryForObject(
				"select * from asignacion where alumno=? AND estado='traspasada'",  
				new Object[] {nombre}, new AsignaMapper());
	}
	
	public Asigna getAsignaRechazada(String nombre) {
		List<Asigna> list = this.jdbcTemplate.query(
				"select * from asignacion where alumno=? AND estado='Rebutjada' ORDER BY fecha_de_la_asignacion DESC",  
				new Object[] {nombre}, new AsignaMapper());
		if(list.isEmpty())
			return null;
		return list.get(0);
	}
		
	public Asigna getAsigna(String nombre,Integer id) {
		String sql = "select A.id_oferta, A.alumno, T.nombre as tutor, A.estado, "
				+ " A.fecha_de_la_asignacion, A.fecha_de_aceptacion, A.fecha_de_traspaso_iglu,"
				+ " A.comentario_peticion_de_cambio, A.fecha_peticion_de_cambio"
		     	+ " from Ofertas_de_practicas O, tutor T, asignacion A "
		     	+ " WHERE A.id_oferta=O.id AND A.tutor=T.alias"
		     	+ " AND A.alumno=? AND A.id_oferta=?"
		     	+ "  ORDER BY A.fecha_peticion_de_cambio";
		return this.jdbcTemplate.queryForObject(
				sql,  
				new Object[] {nombre,id}, new AsignaMapper());
	}
	
	public void addAsigna(Asigna asigna) {
		this.jdbcTemplate.update(
				"insert into Asignacion("
				+ "id_oferta,"
				+ "alumno,"
				+ "tutor,"
				+ "estado,"
				+ "fecha_de_la_asignacion) "
				+ "values(?, ?, ?, 'Pendent', now() )", 
				asigna.getProyecto(),
				asigna.getAlumno(),
				asigna.getProfesor()
				);
	}

	public void acceptAsigna(int proyecto, String alumno)
	{
		this.jdbcTemplate.update(
				"update Asignacion set estado='Acceptada', fecha_de_aceptacion=now() "
				+ "where id_oferta=? and alumno=?", 
				proyecto, alumno);
	}
	public void checkAsigna(Integer proyecto, String alumno)
	{
		this.jdbcTemplate.update(
				"update Asignacion set estado='Confirmada', fecha_de_aceptacion=now() "
				+ "where id_oferta=? and alumno=?", 
				proyecto, alumno);
	}
	public void solicitarCambio(Integer proyecto, String alumno)
	{
		this.jdbcTemplate.update(
				"update Asignacion set estado='peticion_cambio' "
				+ "where id_oferta=? and alumno=?", 
				proyecto, alumno);
	}
	public void rejectAsigna(String comentario, Integer proyecto, String alumno)
	{
		this.jdbcTemplate.update(
				"update Asignacion set estado='Rebutjada', fecha_peticion_de_cambio=now(), comentario_peticion_de_cambio=? "
				+ "where id_oferta=? and alumno=?",
				comentario,
				proyecto, alumno);
	}
	public void moveIGLUAsigna(Integer proyecto, String alumno)
	{
		this.jdbcTemplate.update(
				"update Asignacion set estado='traspasada', fecha_de_traspaso_iglu=now() "
				+ "where id_oferta=? and alumno=?", 
				proyecto, alumno);
	}
	
public void updateAsigna(Asigna asigna) {
	this.jdbcTemplate.update(
			"update Asignacion set"
					+ "id_oferta,"
					+ "estado,"
					+ "fecha_de_la_asignacion,"
					+ "fecha_de_aceptacion,"
					+ "fecha_de_traspaso_iglu,"
					+ "fecha_peticion_de_cambio,"
					+ "comentario_peticion_de_cambio"
			+ " where id_oferta = ? and alumno =?",
			asigna.getProfesor(), asigna.getEstado(), asigna.getFecha_propuesta(),
			asigna.getFecha_aceptacion_o_rechazo(), asigna.getFecha_traspaso_iglu(),
			asigna.getFecha_peticion_cambio(), asigna.getComentario_peticion_cambio(),
			asigna.getProyecto(), asigna.getAlumno());
}

public void updateAsignaPropuesta(Integer proyecto, String alumno) {
this.jdbcTemplate.update(
		"update Asignacion set "
				+ "estado='Pendent',"
				+ "fecha_de_la_asignacion=now()"
		+ " where id_oferta = ? and alumno =?",
		proyecto, alumno);
}
}
