package HelloWorld;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServ extends Remote {
    void chamar(String msg, InterfaceCli iCli) throws RemoteException;

    void registraInteresse(Interesse interesse, InterfaceCli iCli) throws RemoteException;

    void cadastraOferta (Oferta oferta, interfaceCli iCli) throws  RemoteException;

    void alteraOferta (Oferta oferta, interfaceCli iCli) throws RemoteException;
}
