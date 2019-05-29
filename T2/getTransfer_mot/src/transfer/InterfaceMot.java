package transfer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceMot extends Remote {
    int id = 0;

    //notificaInteresse: envia Interesse - cliente com interesse que o motorista pode atender
    void notificaInteresse(Interesse interesse) throws RemoteException;

    //notificaReserva: envia Interesse - quando o cliente reserva o motorista
    void notificaReserva(Interesse interesse) throws RemoteException;
}
