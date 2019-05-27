package transfer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {
    public static void main(String[] args) {
        System.out.println("Hello World Servidor!");

        try {
            Registry referenciaServicoNomes = LocateRegistry.createRegistry(1099);
            InterfaceServ servImpl = new ServImpl();

            referenciaServicoNomes.bind("servImpl",servImpl);

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }

    }
}
