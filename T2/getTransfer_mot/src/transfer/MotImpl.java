package transfer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Queue;
import java.util.Scanner;

public class MotImpl extends UnicastRemoteObject implements InterfaceMot{
    public int id;
    public int i=0;
    public InterfaceServ iSev;
    String[] setTransfer_msgs = {"Tipo Veículo: ", "Número máximo de passageiros: ", "Preço: "};
    String[] setTransfer_args = new String[3];
    Queue<Notificacao> fila_notificacao;

    protected MotImpl(InterfaceServ refServidor, Queue<Notificacao> fila_notificacao_) throws RemoteException {
        System.out.println("MotImpl executado!");
        iSev = refServidor;
        fila_notificacao = fila_notificacao_;
    }

    public void notificaReserva(Interesse interesse) throws RemoteException {
        System.out.println();
        System.out.println(" =======Notificacao==========");
        System.out.println("Reserva efetuada com sucesso!");
        interesse.print();
    }

    public void notificaInteresse(Interesse interesse) throws RemoteException {

        System.out.printf("\n=======> Nova Notificação \n >>");
        fila_notificacao.add(new Notificacao(interesse));
    }




}
