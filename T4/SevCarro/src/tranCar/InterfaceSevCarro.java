package tranCar;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface InterfaceSevCarro extends Remote {

    boolean liberaCarro(int id_carro, int id_clie) throws RemoteException;

    LinkedList<Carro> carrosLivres() throws RemoteException;
}
