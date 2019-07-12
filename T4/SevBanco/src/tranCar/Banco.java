package tranCar;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;

public class Banco {

    public static void main(String[] args) throws IOException, NotBoundException {
        System.out.println("Hello World Banco!");

        // Cria referencia com servidor
        Registry refservicoNomes = LocateRegistry.getRegistry(1099);
        InterfaceSevCarro refServidor = (InterfaceSevCarro) refservicoNomes.lookup("servImpl");

        BancoImpl bancoImpl = new BancoImpl(refServidor);

        int k = 0;
        while(true){
            //print
            System.out.printf("==========BANCO [%3d s]==========\n",k*5);
            try {
                //sleep
                TimeUnit.MILLISECONDS.sleep(5000);
                bancoImpl.printBanco();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("=================================");

            k+=1;
        }
    }
}