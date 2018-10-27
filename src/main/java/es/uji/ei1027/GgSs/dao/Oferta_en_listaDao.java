package es.uji.ei1027.GgSs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.GgSs.modelo.ItinerarioOfertaProyecto;
import es.uji.ei1027.GgSs.modelo.Oferta_de_proyecto;

@Repository
public class Oferta_en_listaDao {
	private JdbcTemplate jdbcTemplate;
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}
	
	/*private int lastOfertas_de_proyecto() {
		return this.jdbcTemplate.queryForObject("select id from Ofertas_de_practicas order by id desc limit 1;",  
				new RowMapper<Integer>() {
	 		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
	 			return rs.getInt("id");
	 		}
		});
	}*/
	private String datos_sql = "id, titulo, descripcion, P.nombre, P.alias as alias_contacto, E.nombre as empresa, O.estado as estado, fecha_alta, fecha_ultima_modificacion, pago, prioridad";
	private static final class OferListMapper implements RowMapper<Oferta_de_proyecto> { 

	    public Oferta_de_proyecto mapRow(ResultSet rs, int rowNum) throws SQLException { 
	        Oferta_de_proyecto oferta = new Oferta_de_proyecto();
	        oferta.setId(rs.getInt("id"));
	        oferta.setTitulo(rs.getString("titulo"));
	        oferta.setDescripcion(rs.getString("descripcion"));
	        oferta.setPersona_de_contacto(rs.getString("nombre"));
	        oferta.setAlias_persona_de_contacto(rs.getString("alias_contacto"));
	        oferta.setEstado(rs.getString("estado"));
	        oferta.setFecha_alta(rs.getDate("fecha_alta"));
	        oferta.setFecha_ultimo_cambio(rs.getDate("fecha_ultima_modificacion"));
	        oferta.setPago(rs.getInt("pago"));
	        oferta.setPrioridad(rs.getInt("prioridad"));
	        oferta.setEmpresa(rs.getString("empresa"));
	        
	        return oferta;
	    }
		
	}



	/*public List<Oferta_en_lista> getOfertas_desde_Itinerario(String itinerario) {
		//TODO itinerario no está bien del todo
		List<Oferta_en_lista> ofertas = this.jdbcTemplate.query(
		     	"select *"
		     	+ " from Ofertas_de_practicas O,persona_de_contacto P, empresa E, Itinerarios_de_la_oferta_de_practicas I"
		     	+ " WHERE O.persona_de_contacto = P.alias AND P.empresa = E.cif AND I.id_oferta = O.id"
		     	+ " AND I.itinerario=? AND O.estado='visibleAlumnos'",
		     	new Object[] {itinerario},new OferListaMapper());
		return ofertas;
	}*/

	public List<Oferta_de_proyecto> getOfertas_desde_Itinerario(String itinerario, String alias_estudiante) {
		//TODO itinerario no está bien del todo
		List<Oferta_de_proyecto> ofertas = this.jdbcTemplate.query(
		     	"select " + datos_sql
		     	+ " from Ofertas_de_practicas O,persona_de_contacto P, empresa E, Itinerarios_de_la_oferta_de_practicas I, Preferencia PA"
		     	+ " WHERE O.persona_de_contacto = P.alias AND P.empresa = E.cif AND I.id_oferta = O.id AND PA.id_oferta = O.id"
		     	+ " AND I.itinerario=? AND PA.alumno=? ORDER BY prioridad",
		     	new Object[] {itinerario, alias_estudiante},new OferListMapper());
		return ofertas;
	}

	public Oferta_de_proyecto getOferta(int id, String alias_estudiante) {
		//TODO itinerario no está bien del todo
		Oferta_de_proyecto oferta = this.jdbcTemplate.queryForObject(
		     	"select " + datos_sql
		     	+ " from Ofertas_de_practicas O,persona_de_contacto P, empresa E, Preferencia PA"
		     	+ " WHERE O.persona_de_contacto = P.alias AND P.empresa = E.cif AND PA.id_oferta = O.id"
		     	+ " AND O.id=? AND PA.alumno=?",
		     	new Object[] {id, alias_estudiante},new OferListMapper());
		oferta.setItinerario(getItinerarios(oferta.getId()));
		return oferta;
	}

	public List<Oferta_de_proyecto> getOfertas(String alias_estudiante) {
		//TODO itinerario no está bien del todo
		return this.jdbcTemplate.query(
		     	"select " + datos_sql
		     	+ " from Ofertas_de_practicas O,persona_de_contacto P, empresa E, Preferencia PA"
		     	+ " WHERE O.persona_de_contacto = P.alias AND P.empresa = E.cif AND PA.id_oferta = O.id"
		     	+ " AND PA.alumno=? ORDER BY prioridad",
		     	new Object[] {alias_estudiante},new OferListMapper());
	}
	

	//Acceso a itinerarios de las ofertas
	private static final class ItinerarioMapper implements RowMapper<ItinerarioOfertaProyecto>{

		@Override
		public ItinerarioOfertaProyecto mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new ItinerarioOfertaProyecto(rs.getInt("id_oferta"),rs.getString("itinerario"));
		}
	}
	
	private List<ItinerarioOfertaProyecto> getItinerarios_de_oferta_de_proyecto(int proyecto) {
		return this.jdbcTemplate.query(
		     	"select * from Itinerarios_de_la_oferta_de_practicas where id_oferta=?", new Object[] {proyecto}, new ItinerarioMapper());
	}
	
	private List<String> getItinerarios(int proyecto) {
		List<ItinerarioOfertaProyecto> lista = getItinerarios_de_oferta_de_proyecto(proyecto);
		List<String> itinerarios = new ArrayList<String>();
		for(int i=0;i<lista.size(); i++)
		{
			itinerarios.add(lista.get(i).getItinerario());
		}
		return itinerarios;
	}

}
