package broker;

import java.util.List;

/**
 *  Estructura de datos que guarda información sobre un servicio.
 */
public class InfoServicio {

	private String nombre_servicio; 
	private List<String> parametros;
	
	public InfoServicio(String nombre_servicio, List<String> parametros){
		this.nombre_servicio = nombre_servicio;
		this.parametros = parametros;
	}

	/**
	 * Devuelve el nombre del servicio.
	 */
	public String getNombre_servicio() {
		return nombre_servicio;
	}

	/**
	 * Devuelve la lista de parámetros del servici.
	 */
	public List<String> getParametros() {
		return parametros;
	}

	
	
	
}
