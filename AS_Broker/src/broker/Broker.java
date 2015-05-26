package broker;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa la versión PRO del Broker.
 *
 */
public class Broker implements InterfazBroker {

	private Registry registroLocal;
	private List<InfoServidor> servidores;
	

	/**
	 * Al crearse un objeto de tipo Broker:
	 * 1. Se registra en su registro RMI local
	 * 2. Crea una nueva estructura de datos de servidores y servicios.
	 */
	public Broker() {

		try {
			// Se genera el stub del broker
			InterfazBroker stub = (InterfazBroker) UnicastRemoteObject
					.exportObject(this, 0);

			// Se obtiene el registro local
			registroLocal = LocateRegistry.getRegistry("localhost", 1099);

			// Se registra el stub en el registro
			try {
				registroLocal.bind("Broker", stub);
				System.err.println("Broker registrado en registro RMI local.");
			} catch (AlreadyBoundException ex) {
				System.err
						.println("Broker ya estaba registrado en el registro RMI local.");
			}

			//Se inicia la estructura de datos
			servidores = new ArrayList<InfoServidor>();
			

		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		}
	}
	
	/**
	 * Busca qué servidor ofrece el servicio con nombre "nom_servicio" y en caso de
	 * que lo ofrezca alguno registrado, lo ejecuta y devuelve el resultado.
	 * En caso contrario devuelve una cadena de error.
	 */
	public String ejecutar_servicio(String nom_servicio,
			List<String> parametros_servicio) throws RemoteException {
		
		//Mira si esta el servicio pedido y obtener el stub
		InterfazServidor stubActual = null;
		for(InfoServidor iservidor: servidores){
			//Para cada servidor
			List<InfoServicio> servicios = iservidor.getInfoServicios();
			for(InfoServicio iservicio: servicios){
				//Para cada servicio que ofrece
				if(iservicio.getNombre_servicio().equalsIgnoreCase(nom_servicio)){
					//Servicio encontrado, salimos del bucle
					stubActual = iservidor.getStubServidor();
					break;
				}
			}
			if(stubActual!=null) break; //Servicio encontrado, no se sigue buscando
			
		}
		
		if(stubActual!=null){
			//Se puede ejecutar el servicio, existe. Se ejecuta.
			String res = stubActual.ejecutar_servicio_local(nom_servicio,parametros_servicio);
			return res;
		}
		else{
			//El servicio no existe. Se devuelve error.
			return "ERROR: El servicio solicitado no es ofrecido por ningún servidor.";
		}
		
	}
	

	/**
	 * Registra el servidor de la dirección "host" con el nombre
	 * "nombre_registrado". Dicho "nombre_registrado" debe ser igual al nombre
	 * con el que el se ha registrado el servidor en su registro RMI local.
	 * Si ya hay un servidor registrado con ese nombre, no se realiza ninguna acción.
	 * El registro del servidor remoto debe estar abierto en el puerto 1099.
	 */
	public void registrar_servidor(String host, String nombre_registrado){
		
		boolean exists = false;
		//Se mira si ya hay un servidor registrado con ese nombre
		for(InfoServidor is: servidores){
			if(is.getNombre_servidor().equalsIgnoreCase(nombre_registrado)){
				exists = true;
				break;
			}
		}
		
		if(!exists){
			//No hay ninguno con ese nombre, se procede al registro
			
			try{
				//Obtenemos el stub del servidor desde su registro
				Registry registroServidor = LocateRegistry.getRegistry(host,1099);
				InterfazServidor stubServidor = (InterfazServidor) registroServidor.lookup(nombre_registrado);
				
				//Añadimos el stub del servidor a la lista de servidores registrados
				InfoServidor nuevoServidor = new InfoServidor(nombre_registrado,stubServidor);
				servidores.add(nuevoServidor);
				System.out.println("Servidor " + nombre_registrado + " registrado con éxito.");
			}
			catch(Exception ex){
				System.err.println("ERROR: Excepción al registrar servidor: " + ex.getMessage());
			}
			
		}
		else{
			System.err.println("ERROR: Ya hay un servidor registrado con nombre " + nombre_registrado);
		}
		
	}

	/**
	 * Registra, para el servidor ya registrado con nombre "nombre_registrado",
	 * el servicio "nombre_servicio" con la lista de parametros "lista_parametros".
	 * Si el servidor indicado no está registrado, no hace nada.
	 * Si el servicio ya estaba registrado para el servidor indicado, no hace nada.
	 */
	
	public void registrar_servicio(String nombre_registrado, String nombre_servicio, List<String> lista_param){
		
		InfoServidor infoServidor = null;
		//Se busca el servidor indicado
		for(InfoServidor is: servidores){
			if(is.getNombre_servidor().equalsIgnoreCase(nombre_registrado)){
				//Se ha encontrado un servidor con dicho nombre ya registrado
				infoServidor = is;
				break;
			}
		}
		if(infoServidor != null){
			//Hay un servidor registrado con ese nombre, le añadimos el servicio.
			infoServidor.addServicio(nombre_servicio, lista_param);
			System.out.println("Servicio " + nombre_servicio + " del servidor " + nombre_registrado + " añadido con éxito.");
		}
		else{
			//No hay un servidor registrado con ese nombre
			System.err.println("ERROR: No hay un servidor registrado con nombre " + nombre_registrado);
		}
		
		
	}

	/**
	 * Devuelve una lista de los servicios registrados en el Broker.
	 */
	public List<String >listar_servicios(){
	List<String> servicios = new ArrayList<>();
		
		for(InfoServidor iservidor: servidores){
			//Para cada servidor
			List<InfoServicio> listaservicios = iservidor.getInfoServicios();
			for(InfoServicio iservicio: listaservicios){
				//Para cada servicio
				String nombre = iservicio.getNombre_servicio();
				List<String> listaparametros = iservicio.getParametros();
				String parametros = "";
				for(String param: listaparametros){
					//Formamos lista de parametros
					parametros+=param + ",";
				}
				//Añadimos nombre de servicio y sus parametros a la lista
				servicios.add(nombre + "(" + parametros + ")");
				
			}
		}
		return servicios;
		
	}

	/**
	 * Método MAIN que lanza un nuevo broker.
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		// Se inicia el broker
		Broker broker = new Broker();
	}

}
