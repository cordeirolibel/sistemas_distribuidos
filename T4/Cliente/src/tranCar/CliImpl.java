package tranCar;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class CliImpl extends UnicastRemoteObject implements tranCar.InterfaceCli {
    public int id;
    tranCar.InterfaceSevCarro iSev;

    public CliImpl(tranCar.InterfaceSevCarro refServidor) throws RemoteException {
        System.out.println("CliImpl executado!");
        iSev = refServidor;
    }

    @Override
    public void echo(String msg) throws RemoteException {
        System.out.printf("Echo enviado para o servidor: %s\n",msg);
    }
}
