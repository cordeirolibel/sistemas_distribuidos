package transfer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface interfaceMot extends Remote {
    Oferta recebeInteresse(InterfaceServ iSev) throws RemoteException;
}
