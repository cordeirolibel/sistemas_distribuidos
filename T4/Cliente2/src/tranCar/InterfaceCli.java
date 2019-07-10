package tranCar;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCli extends Remote {
    int id = 0;
    int id_tran = 0;
    int status_tran = 0;

    void echo(String msg) throws RemoteException;

    void efetiva(int id_tran) throws RemoteException;

    void aborta(int id_tran) throws RemoteException;

}

