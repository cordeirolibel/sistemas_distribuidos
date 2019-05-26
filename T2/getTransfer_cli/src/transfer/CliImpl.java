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
    public Oferta recebeOferta(InterfaceServ iServ) throws RemoteException {
        return null;
    }
}
