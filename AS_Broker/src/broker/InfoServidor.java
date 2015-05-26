package broker;

import java.util.ArrayList;
import java.util.List;

/**
 * Estructura de datos que almacena la información de un servidor registrado
 * en el broker. Contiene su nombre, su stub y una lista de servicios que ofrece.
 *
 */
public class InfoServidor {

	private String nombre_servidor;
	InterfazServidor stubServidor;
	private List<InfoServicio> infoServicios;
	
	public InfoServidor(String nombre_servidor,InterfazServidor stubServidor){
		this.nombre_servidor = nombre_servidor;
		this.stubServidor = stubServidor;
		this.infoServicios = new ArrayList<>();
	}
	
	/**
	 * Devuelve el nombre del servidor.
	 */
	public String getNombre_servidor() {
		return nombre_servidor;
	}
	
	/**
	 * Devuelve el stub del servidor.
	 */
	public InterfazServidor getStubServidor() {
		return stubServidor;
	}
	

	/**
	 * Devuelve la lista de servicios ofrecidos.
	 */
	public List<InfoServicio> getInfoServicios(){
		return infoServicios;
	}
	
	
	/**
	 * Registra para el servidor actual un nuevo servicio. Si ya estaba registrado
	 * dicho servicio, no hace nada.
	 */
	public void addServicio(String nombre_servicio, List<String> parametros){
		
		boolean exists = false;
		//Se comprueba si ya estaba registrado dicho servicio
		for(InfoServicio is: infoServicios){
			if(is.getNombre_servicio().equalsIgnoreCase("nombre_servicio")){
				exists = true;
				break;
			}
		}
		if(!exists){
			//Es un servicio nuevo, se registra.
			InfoServicio is = new InfoServicio(nombre_servicio,parametros);
			infoServicios.add(is);
		}
		else{
			//Servicio ya registrado, no se hace nada.
			System.err.println("ERROR: Servicio " + nombre_servicio + " ya registrado para " + this.getNombre_servidor());
		}
		
	}
}
