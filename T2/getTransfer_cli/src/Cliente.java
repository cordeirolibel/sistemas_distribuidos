package HelloWorld;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente {
    public static void main(String[] args) {
        System.out.println("Hello World Cliente!");

        try {
            Registry refservicoNomes = LocateRegistry.getRegistry(1099);
            InterfaceServ refServidor = (InterfaceServ) refservicoNomes.lookup("servImpl");
            CliImpl cliImpl = new CliImpl(refServidor);

            While (true){

            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
