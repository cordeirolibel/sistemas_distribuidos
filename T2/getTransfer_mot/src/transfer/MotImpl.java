package transfer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Queue;
import java.util.Scanner;

public class MotImpl extends UnicastRemoteObject implements InterfaceMot{
    public int id;
    public int i=0;
    public InterfaceServ iSev;
    Queue<Notificacao> fila_notificacao;

    protected MotImpl(InterfaceServ refServidor, Queue<Notificacao> fila_notificacao_) throws RemoteException {
        System.out.println("MotImpl executado!");
        iSev = refServidor;
        fila_notificacao = fila_notificacao_;
    }

    //notificaReserva: envia Interesse - quando o cliente reserva o motorista
    public void notificaReserva(Interesse interesse) throws RemoteException {
        System.out.println();
        System.out.println(" =======Notificacao==========");
        System.out.println("Reserva efetuada com sucesso!");
        interesse.print();
    }

    //notificaInteresse: envia Interesse - cliente com interesse que o motorista pode atender
    public void notificaInteresse(Interesse interesse) throws RemoteException {

        System.out.printf("\n=======> Nova Notificação \n Option: ");
        fila_notificacao.add(new Notificacao(interesse));
    }




}
