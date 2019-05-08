package HelloWorld;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCli extends Remote {
    void echo(String msg) throws RemoteException;
    Oferta recebeOferta(InterfaceServ iServ) throws  RemoteException;
}

