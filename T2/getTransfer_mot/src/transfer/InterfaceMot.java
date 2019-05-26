package transfer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceMot extends Remote {
    int id = 0;

    void notificaInteresse(Interesse interesse) throws RemoteException;
}
