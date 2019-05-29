package transfer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCli extends Remote {
    int id = 0;

    void echo(String msg) throws RemoteException;

    //notificaOferta: envia Oferta e Interesse (oferta motorista que bate com interesse dele)
    void notificaOferta(Oferta oferta,Interesse interesse) throws  RemoteException;

}

