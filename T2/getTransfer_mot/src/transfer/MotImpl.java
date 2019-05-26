package transfer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MotImpl extends UnicastRemoteObject implements InterfaceMot{


    protected MotImpl() throws RemoteException {
    }

    @Override
    public Oferta recebeInteresse(InterfaceServ iSev) throws RemoteException {
        return null;
    }
}
