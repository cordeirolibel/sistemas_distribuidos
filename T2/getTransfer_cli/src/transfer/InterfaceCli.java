package transfer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCli extends Remote {
    int id = 0;

    void echo(String msg) throws RemoteException;
    void notificaOferta(Oferta oferta) throws  RemoteException;

}

