package transfer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class MotImpl extends UnicastRemoteObject implements InterfaceMot{

    protected MotImpl(InterfaceServ refServidor) throws RemoteException {
        System.out.println("MotImpl executado!");
    }

    public void notificaReserva(Interesse interesse) throws RemoteException {
        System.out.println("Reserva efetuada com sucesso!");
        interesse.print();
    }

    public void notificaInteresse(Interesse interesse) throws RemoteException {
        System.out.println(interesse);
    }
}
