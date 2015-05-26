package holamundo;

import holamundo.Hello;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
public class Server implements Hello {

	public Server() {}

	//Metodo remoto exportado
    public String sayHello() {
        return "Hello, world!";
    }
        
    public static void main(String args[]) {
        
        try {
            Server obj = new Server();
            //Se exporta el stub de Hello. Nos devuelve un stub q es el de los clientes.
            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);

            // Se registra en el registro
            Registry registry = LocateRegistry.getRegistry("localhost",1099);
            registry.bind("Hello", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
