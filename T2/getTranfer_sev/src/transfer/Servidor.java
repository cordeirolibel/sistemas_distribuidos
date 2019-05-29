package transfer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;

public class Servidor {
    public static void main(String[] args)  {
        System.out.println("Hello World Servidor!");
        InterfaceServ servImpl = null;
        try {
            Registry referenciaServicoNomes = LocateRegistry.createRegistry(1099);
            servImpl = new ServImpl();

            referenciaServicoNomes.bind("servImpl",servImpl);

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                //envia notificacoes
                TimeUnit.SECONDS.sleep(1);
                servImpl.enviaNotificacoes();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }


    }
}
