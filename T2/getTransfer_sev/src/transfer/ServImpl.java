package transfer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ{

    public ServImpl() throws RemoteException {

    }

    public void chamar(String msg, InterfaceCli iCli) throws RemoteException{
        iCli.echo(msg);
    }

}
