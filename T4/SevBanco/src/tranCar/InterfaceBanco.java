package tranCar;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceBanco extends Remote {

    boolean debitarValor(int id_clie, float valor, int id_trans) throws RemoteException;

    void efetiva(int id_tran) throws RemoteException;

    void aborta(int id_tran) throws RemoteException;


    void printBanco() throws RemoteException;

}

