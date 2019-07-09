package tranCar;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class CliImpl extends UnicastRemoteObject implements tranCar.InterfaceCli {
    public int id;
    public int id_tran = 0;
    public int status_tran = 0;

    tranCar.InterfaceSevCarro iSev;

    public CliImpl(tranCar.InterfaceSevCarro refServidor) throws RemoteException {
        System.out.println("CliImpl executado!");
        iSev = refServidor;
    }

    @Override
    public void echo(String msg) throws RemoteException {
        System.out.printf("Echo enviado para o servidor: %s\n",msg);
    }

    @Override
    public void efetiva(int id_tran) throws RemoteException {
        System.out.printf("Transacao [id: %s] Efetivada \n", id_tran);
        status_tran = 0;
    }

    @Override
    public void aborta(int id_tran) throws RemoteException {
        System.out.printf("Transacao [id: %s] abortada \n", id_tran);
        status_tran = -1;
    }
}
