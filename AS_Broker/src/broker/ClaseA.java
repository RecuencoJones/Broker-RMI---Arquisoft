package broker;

import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Implementación de la clase A, que ofrece métodos de 'dar_fecha' y 
 * 'dar_hora'.
 * Implementada versión PRO
 *
 */
public class ClaseA implements InterfazServidor {

	
	private String direccionBroker = "localhost"; // IP de registro de Broker
	private Registry registroLocal; // Registro RMI local
	private InterfazBroker stubBroker; //Stub del broker

	/**
	 * Al crear un nuevo objeto de ClaseA:
	 * 1. Se registra en el registro RMI local.
	 * 2. Obtiene el stub del broker.
	 * 3. Registra el servidor "ClaseA" en el broker.
	 * 4. Registra sus servicios "dar_fecha" y "dar_hora" en el broker.
	 */
	public ClaseA() {
		try {
			
			// Se genera el stub de la claseA
			InterfazServidor stub = (InterfazServidor) UnicastRemoteObject.exportObject(this,
					0);

			// Se obtiene el registro local
			registroLocal = LocateRegistry.getRegistry("localhost", 1099);

			// Se registra el stub en el registro
			try {
				registroLocal.bind("ClaseA", stub);
				System.err.println("ClaseA registrada en registro RMI local.");
			} catch (AlreadyBoundException ex) {
				System.err
						.println("ClaseA ya estaba registrada en registro RMI local.");
			}

			
			try {
				// Obtenemos stub del broker
				Registry registroBroker = LocateRegistry.getRegistry(
						direccionBroker, 1099);
				stubBroker = (InterfazBroker) registroBroker.lookup("Broker");
				
				//Registramos el servidor ClaseA
				stubBroker.registrar_servidor("localhost", "ClaseA");
				
				//Registramos el servicio "dar_fecha()"
				stubBroker.registrar_servicio("ClaseA", "dar_fecha", new ArrayList<String>());
				
				//Registramos el servicio "dar_hora()"
				stubBroker.registrar_servicio("ClaseA", "dar_hora", new ArrayList<String>());
				
				
				
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
	 * 'parametros_servicio' haciendo uso del Broker.. Devuelve el resultado como String.
	 */
	public String ejecutar_servicio_remoto(
			String nom_servicio, List<String> parametros_servicio) {
		try {
			//Se llama al broker para que ejecute el servicio
			return stubBroker.ejecutar_servicio(nom_servicio,
					parametros_servicio);
			
		} catch (Exception ex) {
			return "Error al ejecutar el método remoto: " + ex.getMessage();
		}
	}

	/**
	 * Método que ejecuta un servicio local ofrecido por la claseA, a partir
	 * de su nombre "nombre" y su lista de parámetros "parametros".
	 */
	public String ejecutar_servicio_local(String nombre, List<String> parametros) {
		try {
			switch (nombre) {
			case "dar_hora":
				return dar_hora();
			case "dar_fecha":
				return dar_fecha();
			default:
				return "ERROR: Método no existente";

			}
		} catch (Exception ex) {
			return "ERROR en ClaseA al ejecutar métodos";
		}
	}
	
	/**
	 * Método de "dar_hora",que devuelve la hora actual.
	 */
	private String dar_hora() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * Método de "dar_fecha", que devuelve la fecha actual.
	 */
	private String dar_fecha() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * Método que devuelve una lista de servicios ofrecidos por el broker.
	 */
	public List<String> obtener_lista_servicios(){
		try{
			return stubBroker.listar_servicios();
		}
		catch(Exception ex){
			return null;
		}
	}
	
	
	
	
	/** 
	 * Método estático que, a partir de una cadena de tipo "nombreMetodo(param1, param2,)
	 * devuelve el número de parámetros que tiene la función.
	 */
	private static int numeroParametros(String metodo){
		int count = 0;
		for(int i = 0; i < metodo.length(); i ++){
			if(metodo.charAt(i)==','){
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Método Main que crea un objeto de tipo ClaseA, obtiene la lista de servicios
	 * y deja al usuario elegir uno por pantalla.
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {

		// Se pone en marcha la claseA. Se registrará en RMI local y en el broker.
		ClaseA claseA = new ClaseA();
		
		// Se obtiene la lista de servicios de forma dinámica
		List<String> lista = claseA.obtener_lista_servicios();
		System.out.println("Lista de servicios disponibles: ");
		int i = 1;
		for(String e: lista){
			System.out.println("" + i + "- " + e);
			i++;
		}
		
		//Bucle infinito pidiendo ejecucion de metodo
		while(true){
			
			//Se pide al usuario opcion
			System.out.print("\nEscoge un método: (1- " + lista.size()+"): " );
			Scanner scanner = new Scanner(System.in);
			scanner.useDelimiter("\n");
			int opcion = scanner.nextInt();
			
			//Se obtiene nombre de metodo introducido
			String metodo = lista.get(opcion -1);
			int indiceParentesis = metodo.indexOf("(");
			String nombreMetodo = metodo.substring(0, indiceParentesis);
			
			//Se calcula numero parametros segun comas que haya en la definicion
			int numeroPar = numeroParametros(metodo);
			System.out.println("Se va a ejecutar el metodo " + nombreMetodo +" con " + numeroPar + " parametros");
			
			List<String> parametros = new ArrayList<String>();
			while(numeroPar > 0){
				//Se piden los parametros que correspondan
				System.out.print("\nIntroduzca parametro 1: ");
				String par = scanner.next();
				parametros.add(par);
				numeroPar--;
			}
			
			//Se muestra el resultado
			String resultado = claseA.ejecutar_servicio_remoto(nombreMetodo, parametros);
			System.out.println("Resultado de ejecución:\n " + resultado);
			
			}
		

	}
	
}
