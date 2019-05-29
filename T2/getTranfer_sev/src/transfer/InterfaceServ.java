package transfer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface InterfaceServ extends Remote {

    void chamar(String msg, InterfaceCli iCli) throws RemoteException;

    //cotacao: envia Interesse com (itinerário, dia, mes, hora, veículo, passageiros e preço)
    //retorna lista de Oferta de motoristas
    LinkedList<Oferta> cotacao(Interesse interesse) throws RemoteException;

    //envia Oferta e Interesse escolhida
    //retorna true se deu certo
    boolean reserva(Oferta oferta, Interesse interesse) throws RemoteException;

    //registraInteresseCli: envia Interesse(veículo, preço e data) e this
    void registraInteresseCli(Interesse interesse, InterfaceCli iCli) throws RemoteException;

    //envia todas as notificacoes
    void enviaNotificacoes() throws RemoteException;

    //cadastraOferta: envia Oferta (veículo, passageiros e preço) e this
    void cadastraOferta (Oferta oferta, InterfaceMot iMot) throws  RemoteException;

    //novaProposta: envia Interesse e Oferta
    void novaProposta(Oferta oferta,Interesse interesse) throws RemoteException;

    //alteraOferta: envia Oferta (veículo, passageiros e preço) e this
    void alteraOferta (Oferta oferta, InterfaceMot iMot) throws RemoteException;
}
