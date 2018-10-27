package es.uji.ei1027.GgSs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import es.uji.ei1027.GgSs.modelo.Estudiante;


@Repository
public class EstudianteDao {
	 
	private JdbcTemplate jdbcTemplate;
     
	@Autowired
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}

		private static final class EstudianteMapper implements RowMapper<Estudiante> { 

		    public Estudiante mapRow(ResultSet rs, int rowNum) throws SQLException { 
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
		        
		        return estudiante;
		    }
		}

		public List<Estudiante> getEstudiantes() {
			return this.jdbcTemplate.query(
			     	"select * from estudiante;", new EstudianteMapper());
		}
		
		public List<Estudiante> getEstudiantes_con_preferencias(){
			return this.jdbcTemplate.query(
					"select distinct alumno, alias, dni, nombre, apellido, telefono, anyo_academico, itinerario, semestre_elegido, numero_creditos_aprobados "
					+ "from preferencia P, estudiante A where p.alumno = a.alias",
					new EstudianteMapper());
		}
			
		public Estudiante getEstudiante(String alias) {
			return this.jdbcTemplate.queryForObject(
					"select * from estudiante where alias=?",  
					new Object[] {alias}, new EstudianteMapper());
		}
		
		public void addEstudiante(Estudiante estudiante) {
			this.jdbcTemplate.update(
					"insert into Estudiante(alias, dni, nombre, apellido, telefono, anyo_academico, itinerario, semestre_elegido) values(?, ?, ?, ?, ?, ?, ?, ?)", 
					estudiante.getAlias(), estudiante.getDni(), estudiante.getNombre(), 
					estudiante.getApellido(), estudiante.getTelefono(), estudiante.getAnyo_academico(),
					estudiante.getItinerario(), estudiante.getSemestre_inicio_instancia());
		}
			
		public void updateEstudiante(Estudiante estudiante) {
			this.jdbcTemplate.update(
					"update Estudiante set dni=?, nombre=?, apellido=?, telefono=?, anyo_academico=?, itinerario=?, semestre_elegido=? where alias = ?", 
					estudiante.getDni(), estudiante.getNombre(), estudiante.getApellido(), 
					estudiante.getTelefono(), estudiante.getAnyo_academico(), estudiante.getItinerario(), estudiante.getSemestre_inicio_instancia(), estudiante.getAlias());
		}
			
		public void deleteEstudiante(String alias) {
			this.jdbcTemplate.update(
			        "delete from estudiante where alias = ?", alias);
		}
}
