package es.uji.ei1027.GgSs.modelo;

public class ItinerarioOfertaProyecto {
	private int proyecto;
	private String itinerario;
	public ItinerarioOfertaProyecto(int proyect, String iti) {
		proyecto=proyect;
		itinerario=iti;
	}
	public int getProyecto() {
		return proyecto;
	}
	public void setProyecto(int proyecto) {
		this.proyecto = proyecto;
	}
	public String getItinerario() {
		return itinerario;
	}
	public void setItinerario(String itinerario) {
		this.itinerario = itinerario;
	}
}
