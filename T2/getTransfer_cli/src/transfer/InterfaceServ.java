package transfer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface InterfaceServ extends Remote {

    void chamar(String msg, InterfaceCli iCli) throws RemoteException;

    LinkedList<Oferta> cotacao(Oferta oferta) throws RemoteException;

    void reserva(Oferta oferta) throws RemoteException;

    void registraInteresseCli(Interesse interesse, InterfaceCli iCli) throws RemoteException;

    void cadastraOferta (Oferta oferta, InterfaceMot iMot) throws  RemoteException;

    void novaProposta(Oferta oferta,Interesse interesse) throws RemoteException;

    void alteraOferta (Oferta oferta, InterfaceMot iMot) throws RemoteException;
}
