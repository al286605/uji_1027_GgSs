package es.uji.ei1027.GgSs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.GgSs.modelo.Btc;
import es.uji.ei1027.GgSs.modelo.Empresa;
import es.uji.ei1027.GgSs.modelo.Estudiante;
import es.uji.ei1027.GgSs.modelo.Oferta_de_proyecto;
import es.uji.ei1027.GgSs.modelo.PersonaDeContacto;
import es.uji.ei1027.GgSs.modelo.Preferencia_alumno;

@Repository
public class BtcDao {
	private JdbcTemplate jdbcTemplate;
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}
	private static final class BtcMapper implements RowMapper<Btc> { 

	    public Btc mapRow(ResultSet rs, int rowNum) throws SQLException {  
	        Estudiante estudiante = new Estudiante();
	        estudiante.setAlias(rs.getString("alias"));
	        estudiante.setDni(rs.getString("dni"));
	        estudiante.setNombre(rs.getString("nombre"));
	        estudiante.setApellido(rs.getString("apellido"));
	        estudiante.setTelefono(rs.getString("telefono"));
	        estudiante.setAnyo_academico(rs.getInt("anyo_academico"));
	        estudiante.setNumero_de_creditos_aprobados(rs.getInt("numero_creditos_aprobados"));
	        estudiante.setItinerario(rs.getString("itinerario"));
	        estudiante.setSemestre_inicio_instancia(rs.getInt("semestre_elegido"));
	        
	        Preferencia_alumno preferencia = new Preferencia_alumno();
	        preferencia.setAlumno(rs.getString("alumno"));
	        preferencia.setOferta(rs.getInt("id_oferta"));
	        preferencia.setPrioridad(rs.getInt("prioridad"));
	        preferencia.setEstado(rs.getString("estado"));
	        
	        Oferta_de_proyecto oferta = new Oferta_de_proyecto();
	        oferta.setId(rs.getInt("id"));
	        oferta.setTitulo(rs.getString("titulo"));
	        oferta.setDescripcion(rs.getString("descripcion"));
	        oferta.setPersona_de_contacto(rs.getString("persona_de_contacto"));
	        oferta.setEstado(rs.getString("estado"));
	        oferta.setFecha_alta(rs.getDate("fecha_alta"));
	        oferta.setFecha_ultimo_cambio(rs.getDate("fecha_ultima_modificacion"));
	        oferta.setPago(rs.getInt("pago"));
	        
	        PersonaDeContacto contacto = new PersonaDeContacto();
	        contacto.setAlias(rs.getString("alias"));
	        contacto.setNombre(rs.getString("nombre"));
	        contacto.setEmpresa(rs.getString("empresa"));
	        contacto.setPuesto(rs.getString("puesto"));
	        
	        Empresa empresa = new Empresa();
	        empresa.setCif(rs.getString("cif"));
	        empresa.setNombre(rs.getString("nombre"));
	        empresa.setTelefono(rs.getString("telefono"));
	        empresa.setDireccion(rs.getString("direccion"));
	        
	        Btc btc = new Btc();
	        btc.contacto=contacto;
	        btc.empresa=empresa;
	        btc.estudiante=estudiante;
	        btc.oferta=oferta;
	        btc.preferencia=preferencia;
	        return btc;
	    }
	}

	public List<Btc> getAsignarOfertas() {
		return this.jdbcTemplate.query(
		     	"select * "
		     	+ "from Estudiante Est, Preferencia_alumno PR, Ofertas_de_proyectos O,persona_de_contacto P, empresa E "
		     	+ "WHERE O.persona_de_contacto = P.alias AND P.empresa = E.cif AND PR.id_oferta = O.id AND PR.alumno = Est.alias",
		     	new BtcMapper());
	}

	public List<Btc> getAsignarOfertas(String alumno) {
		return this.jdbcTemplate.query(
		     	"select * "
		     	+ "from Estudiante Est, Preferencia PR, Ofertas_de_proyectos O,persona_de_contacto P, empresa E "
		     	+ "WHERE O.persona_de_contacto = P.alias AND P.empresa = E.cif AND PR.id_oferta = O.id AND PR.alumno = Est.alias"
		     	+ "AND Est.alias=?",
		     	new Object[] {alumno},new BtcMapper());
	}
		
	public Btc getAsignarOferta(String alumno, int oferta) {
		return this.jdbcTemplate.queryForObject(
		     	"select * "
		     	+ "from Estudiante Est, Preferencia PR, Ofertas_de_proyectos O,persona_de_contacto P, empresa E "
		     	+ "WHERE O.persona_de_contacto = P.alias AND P.empresa = E.cif AND PR.id_oferta = O.id AND PR.alumno = Est.alias"
		     	+ "AND Est.alias=? AND O.id=?",  
				new Object[] {alumno,oferta}, new BtcMapper());
	}
	
	/*public void addPreferencia_alumno(Preferencia_alumno preferencia) {
		this.jdbcTemplate.update(
				"insert into Preferencia_alumno( alumno, oferta, prioridad, estado) values(?, ?, ?, ?)", 
				preferencia.getAlumno(), preferencia.getOferta(), preferencia.getPrioridad(), preferencia.getEstado());
	}
		
	public void updatePreferencia_alumno(Preferencia_alumno preferencia) {
		this.jdbcTemplate.update(
				"update Preferencia_alumno set prioridad=?, estado=? where alumno=? and oferta=?",
				 preferencia.getPrioridad(), preferencia.getEstado(), preferencia.getAlumno(), preferencia.getOferta() );
	}
		
	public void deletePreferencia_alumno(String alumno, int oferta) {
		this.jdbcTemplate.update(
		        "delete from Preferencia_alumno where alumno = ? and oferta = ?", alumno, oferta);
	}*/
}
