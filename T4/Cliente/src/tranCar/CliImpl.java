package tranCar;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class CliImpl extends UnicastRemoteObject implements tranCar.InterfaceCli {
    public int id;
    public int id_tran = 0;
    public int status_tran = 0;

    LinkedList<Transacao> lista_transacao;

    tranCar.InterfaceSevCarro iSev;

    public CliImpl(tranCar.InterfaceSevCarro refServidor) throws RemoteException {
        System.out.println("CliImpl executado!");
        iSev = refServidor;

        lista_transacao = new LinkedList<Transacao>();
    }

    Transacao abreTransacao(Carro carro, InterfaceCli clieImpl) {
        //cria transacao
        Transacao transacao = new Transacao();

        transacao.setId_clie(clieImpl.id);
        lista_transacao.add(transacao);

        return transacao;
    }

    //finaliza a transacao, liberando os recursos
    public void efetiva(int id_tran) {

        //busca transacao e carro
        Transacao transacao = buscaTransacao(id_tran);

        transacao.efetiva();

        System.out.printf(" => transacao %d efetivada\n",transacao.getId_tran());

        //remove a transacao
        lista_transacao.remove(transacao);
    }

    public void aborta(int id_tran){

        //busca transacao e carro
        Transacao transacao = buscaTransacao(id_tran);

        transacao.aborta();

        System.out.printf(" => transacao %d abortada\n",transacao.getId_tran());

        //remove a transacao
        lista_transacao.remove(transacao);
    }

    public Transacao buscaTransacao(int id_tran){

        int size = lista_transacao.size();
        Transacao transacao;

        //procura as transacoes na lista
        for (int i=0;i<size;i++) {
            transacao = lista_transacao.get(i);
            if (transacao.getId_tran() == id_tran){
                return transacao;
            }
        }

        return null;
    }

    @Override
    public void echo(String msg) throws RemoteException {
        System.out.printf("Echo enviado para o servidor: %s\n",msg);
    }
}
