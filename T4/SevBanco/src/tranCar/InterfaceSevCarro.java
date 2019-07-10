package tranCar;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface InterfaceSevCarro extends Remote {


    //retorna o estado da transacao
    //efetivada, cancelada ou provisoria.
    public String obtemStatus(int id_tran) throws RemoteException;

    void ref_banco(InterfaceBanco bancoImpl) throws RemoteException;

}
