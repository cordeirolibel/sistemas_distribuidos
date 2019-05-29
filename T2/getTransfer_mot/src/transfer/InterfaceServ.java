package transfer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface InterfaceServ extends Remote {

    //cadastraOferta: envia Oferta (veículo, passageiros e preço) e this
    void cadastraOferta (Oferta oferta, InterfaceMot iMot) throws  RemoteException;

    //novaProposta: envia Interesse e Oferta
    void novaProposta(Oferta oferta,Interesse interesse) throws RemoteException;

    //alteraOferta: envia Oferta (veículo, passageiros e preço) e this
    void alteraOferta (Oferta oferta, InterfaceMot iMot) throws RemoteException;
}
