package transfer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MotImpl extends UnicastRemoteObject implements InterfaceMot{

    protected MotImpl(InterfaceServ refServidor) throws RemoteException {
    }

    public void notificaReserva(Interesse interesse) throws RemoteException {

    }

    public void notificaInteresse(Interesse interesse) throws RemoteException {

    }
}
