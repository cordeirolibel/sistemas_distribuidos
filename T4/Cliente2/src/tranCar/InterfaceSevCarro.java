package tranCar;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface InterfaceSevCarro extends Remote {

    int liberaCarro(int id_carro, int id_clie,InterfaceCli clieImpl) throws RemoteException;

    LinkedList<Carro> carrosLivres() throws RemoteException;

	//retorna o estado da transacao
    //efetivada, cancelada ou provisoria.
    public String obtemStatus(int id_tran) throws RemoteException;

    void chamar(String msg, InterfaceCli iCli) throws RemoteException;
}
