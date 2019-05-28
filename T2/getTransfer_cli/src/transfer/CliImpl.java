package transfer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli{

    public CliImpl(InterfaceServ refServidor) throws RemoteException {

    }


    @Override
    public void echo(String msg) throws RemoteException {
        System.out.printf("Echo enviado para o servidor: %s\n",msg);
    }

    @Override
    public void notificaOferta(Oferta oferta) throws RemoteException {
        return;
    }
}
