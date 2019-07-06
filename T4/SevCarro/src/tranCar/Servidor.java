package tranCar;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;

public class Servidor {
    public static void main(String[] args)  {
        System.out.println("Hello World Servidor!");
        InterfaceSevCarro servImpl = null;

        try {
            //Cria servidor
            Registry referenciaServicoNomes = LocateRegistry.createRegistry(1099);
            servImpl = new SevImpl();
            referenciaServicoNomes.bind("servImpl",servImpl);

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }

        //envia notificacoes a cada um segundo
        while(true){}
    }
}
