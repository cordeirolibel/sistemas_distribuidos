package HelloWorld;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli{
    InterfaceServ refServidor;

    public CliImpl(InterfaceServ refServidor)throws RemoteException {
        refServidor.chamar("Ola",this);
    }

    public void echo(String msg) throws RemoteException {
        System.out.printf("Echo do Cliente: %s\n",msg);
    }

    public void cadastraInteresse (Interesse interesse, InterfaceServ refServidor) throws RemoteException{
        // String itinerario, Long data, String tipoVeic, int nPass, int preco
        refServidor.registraInteresse(interesse);
    }

    Oferta recebeOferta (interfaceServ iSev) throws RemoteException{
        //
    }

}
