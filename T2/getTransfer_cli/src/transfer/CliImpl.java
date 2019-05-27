package transfer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli{

    public CliImpl() throws RemoteException {

    }


    @Override
    public void echo(String msg) throws RemoteException {

    }

    @Override
    public Oferta notificaOferta(Oferta oferta) throws RemoteException {
        return null;
    }
}
