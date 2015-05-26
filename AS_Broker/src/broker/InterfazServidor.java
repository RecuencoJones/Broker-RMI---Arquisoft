package broker;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interfaz remota que debe implementar un servidor que quiera registrarse en el
 * broker.
 */
public interface InterfazServidor extends Remote {

	/**
	 * Método que ejecuta un servicio local del servidor registrado en el broker.
	 * Ejecuta el método indicado por "nombre_servicio" con los parámetros
	 * "parametros_servicio".
	 */
	String ejecutar_servicio_local(String nombre_servicio,
			List<String> parametros_servicio) throws RemoteException;

}
