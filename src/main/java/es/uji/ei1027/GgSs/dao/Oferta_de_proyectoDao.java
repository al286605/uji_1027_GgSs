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
import es.uji.ei1027.GgSs.modelo.Oferta_de_proyecto;

@Repository
public class Oferta_de_proyectoDao {
	private JdbcTemplate jdbcTemplate;
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}
	
	private int lastOfertas_de_proyecto() {
		return this.jdbcTemplate.queryForObject("select id from Ofertas_de_practicas order by id desc limit 1;",  
				new RowMapper<Integer>() {
	 		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
	 			return rs.getInt("id");
	 		}
		});
	}
	
	private String datos_sql = "id, titulo, descripcion, P.nombre, P.alias as alias_contacto, E.nombre as empresa, estado, fecha_alta, fecha_ultima_modificacion, pago";
	private static final class ContactoMapper implements RowMapper<Oferta_de_proyecto> { 

	    public Oferta_de_proyecto mapRow(ResultSet rs, int rowNum) throws SQLException { 
	        Oferta_de_proyecto oferta = new Oferta_de_proyecto();
	        oferta.setId(rs.getInt("id"));
	        oferta.setTitulo(rs.getString("titulo"));
	        oferta.setDescripcion(rs.getString("descripcion"));
	        oferta.setPersona_de_contacto(rs.getString("nombre"));
	        oferta.setAlias_persona_de_contacto(rs.getString("alias_contacto"));
	        oferta.setEmpresa(rs.getString("empresa"));
	        oferta.setEstado(rs.getString("estado"));
	        oferta.setFecha_alta(rs.getDate("fecha_alta"));
	        oferta.setFecha_ultimo_cambio(rs.getDate("fecha_ultima_modificacion"));
	        oferta.setPago(rs.getInt("pago"));
	        
	        return oferta;
	    }
		
	}

	public List<Oferta_de_proyecto> getOfertas_de_proyecto() {
		List<Oferta_de_proyecto> ofertas = this.jdbcTemplate.query(
				"select "+ datos_sql +" from Ofertas_de_practicas O,persona_de_contacto P, empresa E "
				+ " WHERE O.persona_de_contacto = P.alias AND P.empresa = E.cif "
						, new ContactoMapper());
		for(int i=0; i<ofertas.size(); i++)
		{
			Oferta_de_proyecto oferta = ofertas.get(i);
			oferta.setItinerario(getItinerarios(oferta.getId()));
		}
		return ofertas;
	}

	public List<Oferta_de_proyecto> getOfertas_de_proyecto(String empresa) {
		//TODO empresas no está bien del todo
		List<Oferta_de_proyecto> ofertas = this.jdbcTemplate.query(
		     	"select "+datos_sql+" from Ofertas_de_practicas O,persona_de_contacto P, empresa E WHERE O.persona_de_contacto = P.alias AND P.empresa = E.cif AND E.cif=?",
		     	new Object[] {empresa},new ContactoMapper());
		//List<Oferta_de_proyecto> ofertas = this.jdbcTemplate.query(
		//     	"select * from ofertas_de_practicas", new Object[] {alias}, new ContactoMapper());
		for(int i=0; i<ofertas.size(); i++)
		{
			Oferta_de_proyecto oferta = ofertas.get(i);
			oferta.setItinerario(getItinerarios(oferta.getId()));
		}
		return ofertas;
	}

	public List<Oferta_de_proyecto> getOfertas_de_proyectoItinerario(String itinerario) {
		//TODO itinerario no está bien del todo
		List<Oferta_de_proyecto> ofertas = this.jdbcTemplate.query(
		     	"select " + datos_sql
		     	+ " from Ofertas_de_practicas O,persona_de_contacto P, empresa E, itinerarios_de_la_oferta_de_practicas I"
		     	+ " WHERE O.persona_de_contacto = P.alias AND P.empresa = E.cif AND I.id_oferta = O.id"
		     	+ " AND I.itinerario=? AND O.estado='Visible per alumnes'",
		     	new Object[] {itinerario},new ContactoMapper());
		for(int i=0; i<ofertas.size(); i++)
		{
			Oferta_de_proyecto oferta = ofertas.get(i);
			oferta.setItinerario(getItinerarios(oferta.getId()));
		}
		return ofertas;
	}

	public List<Oferta_de_proyecto> getOfertas_de_proyectoEstado(String estado) {
		List<Oferta_de_proyecto> ofertas = this.jdbcTemplate.query(
		     	"select "+datos_sql+" from Ofertas_de_practicas O,persona_de_contacto P, empresa E WHERE O.persona_de_contacto = P.alias AND P.empresa = E.cif AND O.estado=?",new Object[] {estado}, new ContactoMapper());
		for(int i=0; i<ofertas.size(); i++)
		{
			Oferta_de_proyecto oferta = ofertas.get(i);
			oferta.setItinerario(getItinerarios(oferta.getId()));
		}
		return ofertas;
	}
	public List<Oferta_de_proyecto> getOfertas_de_proyectoPendientesDeRevisar() {
		String estado = "Pendent de revisió";
		return getOfertas_de_proyectoEstado(estado);
	}

	public List<Oferta_de_proyecto> getOfertas_de_proyectoAceptadas() {
		String estado = "Acceptada";
		return getOfertas_de_proyectoEstado(estado);
	}

	public List<Oferta_de_proyecto> getOfertas_de_proyectoVisible() {
		String estado = "Visible per alumnes";
		return getOfertas_de_proyectoEstado(estado);
	}
		
	public Oferta_de_proyecto getOferta_de_proyecto(Integer id) {
		Oferta_de_proyecto oferta = this.jdbcTemplate.queryForObject(
				"select "+datos_sql+" from Ofertas_de_practicas O,persona_de_contacto P, empresa E "
				+ " WHERE O.persona_de_contacto = P.alias AND P.empresa = E.cif "+
				"AND id=?",  
				new Object[] {id}, new ContactoMapper());
		oferta.setItinerario(getItinerarios(oferta.getId()));
		return oferta;
	}
	
	public void addOferta_de_proyecto(Oferta_de_proyecto oferta){
		this.jdbcTemplate.update(
				"insert into Ofertas_de_practicas("
				+ " titulo,"
				+ " descripcion,"
				+ " persona_de_contacto,"
				+ " estado,"
				+ " fecha_alta,"
				+ " fecha_ultima_modificacion,"
				+ " pago)"
				+ " values(?, ?, ?, ?, now(), now(), ?)", 
				oferta.getTitulo(),
				oferta.getDescripcion(),
				oferta.getPersona_de_contacto(),
				oferta.getEstado(),
				oferta.getPago());
		deleteItinerario_de_oferta_de_proyecto(oferta.getId());
		addItinerarios(lastOfertas_de_proyecto(), oferta.getItinerario().iterator());
	}
		
	public void updateOferta_de_proyecto(Oferta_de_proyecto oferta) {
		this.jdbcTemplate.update(
				"update Ofertas_de_practicas set"
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
		deleteItinerario_de_oferta_de_proyecto(oferta.getId());
		addItinerarios(oferta.getId(),oferta.getItinerario().iterator());
	}
		
	public void deleteOferta_de_proyecto(int id) {
		deleteItinerario_de_oferta_de_proyecto(id);
		this.jdbcTemplate.update(
		        "delete from Ofertas_de_practicas where id = ?", id);
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
	
	private void addItinerario(ItinerarioOfertaProyecto iti) {
		this.jdbcTemplate.update(
				"insert into Itinerarios_de_la_oferta_de_practicas(id_oferta, itinerario) values(?, ?)", 
				iti.getProyecto(), iti.getItinerario());
	}
	
	private void addItinerarios(int id, Iterator<String> iter) {
		while(iter.hasNext())
		{
			String iti = iter.next();
			addItinerario(new ItinerarioOfertaProyecto(id, iti));
		}
	}
		
	private void deleteItinerario_de_oferta_de_proyecto(int id) {
		this.jdbcTemplate.update(
		        "delete from Itinerarios_de_la_oferta_de_practicas where id_oferta = ?", id);
	}
}
