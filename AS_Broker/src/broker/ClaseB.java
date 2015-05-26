package broker;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa la InterfazB, con metodos para operar con libros.
 * Implementada versión PRO.
 */
public class ClaseB implements InterfazServidor {

	
	private String direccionBroker = "localhost"; // IP de registro de Broker

	private InterfazBroker stubBroker; // Stub del broker

	private List<String> listaLibros; // Lista de libros

	/**
	 * Al crear un nuevo objeto de ClaseB:
	 * 1. Se registra en el registro RMI local.
	 * 2. Obtiene el stub del broker.
	 * 3. Registra el servidor "ClaseB" en el broker.
	 * 4. Registra sus servicios "lista_libros" e "introducir_libro" en el broker.
	 */
	public ClaseB() {
		try {
			// Se genera la lista vacia de libros
			listaLibros = new ArrayList<String>();

			// Se genera el stub de la claseB
			InterfazServidor stub = (InterfazServidor) UnicastRemoteObject
					.exportObject(this, 0);

			// Se obtiene el registro local
			Registry registroLocal = LocateRegistry.getRegistry("localhost",
					1099);

			// Se registra el stub en el registro local
			try {
				registroLocal.bind("ClaseB", stub);
				System.err.println("ClaseB registrada en registro RMI local.");
			} catch (AlreadyBoundException ex) {
				System.err
						.println("ClaseB ya estaba registrada en registro RMI local.");
			}

			
			try {
				// Obtenemos stub del broker remoto
				Registry registroBroker = LocateRegistry.getRegistry(
						direccionBroker, 1099);
				stubBroker = (InterfazBroker) registroBroker.lookup("Broker");
				
				//Registramos el servidor en el broker
				stubBroker.registrar_servidor("localhost", "ClaseB");
				
				//Registramos el servicio "introducir_libro"
				List<String> param1 = new ArrayList<>();
				param1.add("String titulo");
				stubBroker.registrar_servicio("ClaseB", "introducir_libro", param1);
				
				//Registramos el servicio "lista_libros"
				stubBroker.registrar_servicio("ClaseB", "lista_libros", new ArrayList<String>());
				
			} catch (Exception ex) {
				System.err.println("Error al obtener stub del Broker: "
						+ ex.getMessage());
			}
		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		}

	}

	/**
	 * Ejecuta el servicio remoto  con nombre 'nom_servicio' y parametros 
	 * 'parametros_servicio' haciendo uso del Broker. Devuelve el resultado como String.
	 */
	public String ejecutar_servicio_remoto(String nom_servicio,
			List<String> parametros_servicio) {
		try {
			//Llama al broker para ejecutar el servicio remoto
			return stubBroker.ejecutar_servicio(nom_servicio,
					parametros_servicio);
		} catch (Exception ex) {
			return "Error al ejecutar el método remoto: " + ex.getMessage();
		}
	}

	
	/**
	 * Método que introduce un libro con titulo "libro" en la colección.
	 */
	private String introducir_libro(String libro) throws RemoteException {
		listaLibros.add(libro);
		return "OK";
	}

	/**
	 * Método que devuelve una lista de libros disponibles en la colección.
	 */
	private String lista_libros() throws RemoteException {
		String lista = "";
		int index = 0;
		for (String libro : listaLibros) {
			if (index == 0) {
				lista += libro;
			} else {
				lista += "\n" + libro;
			}
			index++;
		}
		return lista;
	}

	/**
	 * Método que ejecuta un servicio local ofrecido por la claseB, a partir
	 * de su nombre "nombre" y su lista de parámetros "parametros".
	 */
	public String ejecutar_servicio_local(String nombre, List<String> parametros) {
		try {
			switch (nombre) {
			case "lista_libros":
				return lista_libros();
			case "introducir_libro":
				return introducir_libro(parametros.get(0));
			default:
				return "ERROR: Método no existente";

			}
		} catch (Exception ex) {
			return "ERROR en ClaseB al ejecutar métodos";
		}
	}
	
	/**
	 * Método Main, que lanza un servidor 'ClaseB', que se registrará en el RMI local
	 * y en el broker, junto a los servicios que ofrece.
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		//Nuevo objeto ClaseB.
		ClaseB claseB = new ClaseB();


	}
}
