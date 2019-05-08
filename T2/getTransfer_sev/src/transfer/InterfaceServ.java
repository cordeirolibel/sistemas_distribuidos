package transfer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServ extends Remote {
    void chamar(String msg, InterfaceCli iCli) throws RemoteException;

    void registraInteresse(Interesse interesse, InterfaceCli iCli) throws RemoteException;

    void cadastroOferta(Oferta oferta, InterfaceCli iCli) throws RemoteException;

    void alteraOferta(Oferta oferta, InterfaceCli iCli) throws RemoteException;


}
