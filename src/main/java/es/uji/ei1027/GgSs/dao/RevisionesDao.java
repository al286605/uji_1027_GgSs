package es.uji.ei1027.GgSs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.GgSs.modelo.ItinerarioOfertaProyecto;
import es.uji.ei1027.GgSs.modelo.Revision;

@Repository
public class RevisionesDao {
	private JdbcTemplate jdbcTemplate;
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}
	
	private int lastRevisiones() {
		return this.jdbcTemplate.queryForObject("select id from Revisiones_de_la_oferta_de_practicas order by id desc limit 1;",  
				new RowMapper<Integer>() {
	 		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
	 			return rs.getInt("id");
	 		}
		});
	}
	
	private String datos_sql = "id_oferta, descripcion, mensaje_de_revision, fecha_solicitud";
	private static final class ContactoMapper implements RowMapper<Revision> { 

		public Revision mapRow(ResultSet rs, int rowNum) throws SQLException { 
	        Revision oferta = new Revision();
	        oferta.setId(rs.getInt("id_oferta"));
	        oferta.setDescripcion(rs.getString("descripcion"));
	        oferta.setMensaje(rs.getString("mensaje_de_revision"));
	        oferta.setFecha(rs.getDate("fecha_solicitud"));
	        
	        return oferta;
	    }
	}

	public List<Revision> getRevisiones() {
		List<Revision> revision = this.jdbcTemplate.query(
				"select "+ datos_sql +" from Revisiones_de_la_oferta_de_practicas O "
						, new ContactoMapper());
		return revision;
	}
		
	public List<Revision> getRevision(int id) {
		List<Revision> revision = this.jdbcTemplate.query(
				"select "+datos_sql+" from Revisiones_de_la_oferta_de_practicas O "
				+ "WHERE id_oferta=?",  
				new Object[] {id}, new ContactoMapper());
		return revision;
	}
	
	public void addRevision(Revision revision){
		this.jdbcTemplate.update(
				"insert into Revisiones_de_la_oferta_de_practicas("
				+ " id_oferta,"
				+ " descripcion,"
				+ " mensaje_de_revision,"
				+ " fecha_solicitud)"
				+ " values(?, ?, ?, now())", 
				revision.getId(),
				revision.getDescripcion(),
				revision.getMensaje());
	}
		/*
	public void updateRevision(Revision oferta) {
		this.jdbcTemplate.update(
				"update Revisiones_de_la_oferta_de_practicas set"
				+ " titulo=?,"
				+ " descripcion=?,"
				+ " persona_de_contacto=?,"
				+ " estado=?,"
				+ " fecha_ultima_modificacion=now(),"
				+ " pago=?"
				+ " where id = ?", 
				oferta.getTitulo(),
				oferta.getDescripcion(),
				oferta.getAlias_persona_de_contacto(),
				oferta.getEstado(),
				oferta.getPago(),
				oferta.getId());
		deleteItinerario_de_Revision(oferta.getId());
		addItinerarios(oferta.getId(),oferta.getItinerario().iterator());
	}
		
	public void deleteRevision(int id) {
		deleteItinerario_de_Revision(id);
		this.jdbcTemplate.update(
		        "delete from Revisiones_de_la_oferta_de_practicas where id = ?", id);
	}
	*/
	
}
