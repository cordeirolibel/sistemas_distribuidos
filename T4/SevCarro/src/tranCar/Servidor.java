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

        //Verifica se tem transacaos pendentes
        int k = 0;
        while(true){
            try {
                //Verifica se tem transacaos pendentes
                servImpl.atualizaTransacoes();
                //sleep
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //print
            if ((k%50) == 0){
                System.out.printf("[%3d s]\n",k/10);
            }
            k+=1;
        }
    }
}
