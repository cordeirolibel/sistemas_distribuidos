package transfer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface InterfaceServ extends Remote {



    void chamar(String msg, InterfaceCli iCli) throws RemoteException;

    LinkedList<Oferta> cotacao(Interesse interesse) throws RemoteException;

    boolean reserva(Oferta oferta, Interesse interesse) throws RemoteException;

    void registraInteresseCli(Interesse interesse, InterfaceCli iCli) throws RemoteException;

    void enviaNotificacoes() throws RemoteException;

    void cadastraOferta (Oferta oferta, InterfaceMot iMot) throws  RemoteException;

    void novaProposta(Oferta oferta,Interesse interesse) throws RemoteException;

    void alteraOferta (Oferta oferta, InterfaceMot iMot) throws RemoteException;
}
