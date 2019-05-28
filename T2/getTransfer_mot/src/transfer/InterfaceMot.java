package transfer;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceMot extends Remote, Serializable {
    int id = 0;

    void notificaInteresse(Interesse interesse) throws RemoteException;

    void notificaReserva(Interesse interesse) throws RemoteException;
}
