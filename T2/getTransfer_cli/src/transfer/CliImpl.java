package transfer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli{
    public int id;
    InterfaceServ iSev;
    Queue<Notificacao> fila_notificacao = new LinkedList<Notificacao>();


    public CliImpl(InterfaceServ refServidor) throws RemoteException {
        System.out.println("CliImpl executado!");
        iSev = refServidor;
    }


    @Override
    public void echo(String msg) throws RemoteException {
        System.out.printf("Echo enviado para o servidor: %s\n",msg);
    }

    @Override
    public void notificaOferta(Oferta oferta,Interesse interesse) throws RemoteException {
        System.out.printf("\n=======> Nova Notificação \n >> ");
        fila_notificacao.add(new Notificacao(oferta,interesse));
    }
}
