package tranCar;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Banco {

    public static void main(String[] args) throws IOException, NotBoundException {
        System.out.println("Hello World Banco!");

        // Cria referencia com servidor
        Registry refservicoNomes = LocateRegistry.getRegistry(1099);
        InterfaceSevCarro refServidor = (InterfaceSevCarro) refservicoNomes.lookup("servImpl");

        BancoImpl bancoImpl = new BancoImpl(refServidor);

        while(true);
    }
}