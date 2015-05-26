package broker;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interfaz remota del servicio ofrecido por el broker.
 *
 */
public interface InterfazBroker extends Remote {

	/**
	 * Ejecuta el servicio "nom_servicio" con los parametros
	 * "parametros_servicio"
	 */
	String ejecutar_servicio(String nom_servicio,
			List<String> parametros_servicio) throws RemoteException;

	/**
	 * Registra el servidor de la dirección "host" con el nombre
	 * "nombre_registrado". Dicho "nombre_registrado" debe ser igual al nombre
	 * con el que el se ha registrado el servidor en su registro local.
	 * Si ya hay un servidor registrado con ese nombre, no se realiza ninguna acción.
	 * El registro del servidor remoto debe estar abierto en el puerto 1099.
	 */
	void registrar_servidor(String host, String nombre_registrado)
			throws RemoteException;

	/**
	 * Registra, para el servidor ya registrado con nombre "nombre_registrado",
	 * el servicio "nombre_servicio" con la lista de parametros "lista_parametros".
	 * Si el servidor indicado no está registrado, no hace nada.
	 * Si el servicio con dicho nombre ya estaba registrado para dicho servidor,
	 * no hace nada.
	 */
	void registrar_servicio(String nombre_registrado, String nombre_servicio,
			List<String> lista_parametros) throws RemoteException;
	
	
	/**
	 * Devuelve una lista de los servicios registrados en el Broker.
	 */
	List<String> listar_servicios() throws RemoteException;

}
