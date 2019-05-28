package transfer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli{
    public int id;

    public CliImpl(InterfaceServ refServidor) throws RemoteException {
        System.out.println("CliImpl executado!");
    }


    @Override
    public void echo(String msg) throws RemoteException {
        System.out.printf("Echo enviado para o servidor: %s\n",msg);
    }

    @Override
    public void notificaOferta(Oferta oferta) throws RemoteException {
        oferta.print();


    }
}
