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

    LinkedList<Transacao> lista_transacao;

    tranCar.InterfaceSevCarro iSev;

    public CliImpl(tranCar.InterfaceSevCarro refServidor) throws RemoteException {
        System.out.println("CliImpl executado!");
        iSev = refServidor;

        lista_transacao = new LinkedList<Transacao>();

        loadTransacoes();
        atualizaTransacoes();
    }


    //finaliza a transacao, liberando os recursos
    public void efetiva(int id_tran) throws RemoteException  {

        //busca transacao e carro
        Transacao transacao = buscaTransacao(id_tran);

        // LOG: transação efetivada
        transacao.efetiva();

        System.out.printf(" => transacao %d efetivada\n",transacao.getId_tran());

        //remove a transacao
        lista_transacao.remove(transacao);
    }

    public void aborta(int id_tran) throws RemoteException {

        //busca transacao e carro
        Transacao transacao = buscaTransacao(id_tran);

        // LOG: Transação abortada
        transacao.aborta();

        System.out.printf(" => transacao %d abortada\n",transacao.getId_tran());

        //remove a transacao
        lista_transacao.remove(transacao);
    }

    //==============================
    // Internas

    public void abreTransacao(int cli_id) {
        //cria transacao
        Transacao transacao = new Transacao();

        transacao.setId_clie(cli_id);
        lista_transacao.add(transacao);
    }

    //verifica se tem alguma transacao pendente e atualiza
    public void atualizaTransacoes() throws RemoteException {
        int size = lista_transacao.size();

        Transacao transacao;
        String status;

        //procura os transacoes em aberto
        for (int i=0;i<size;i++) {
            transacao = lista_transacao.get(i);
            if (transacao.getStatus().equals("provisoria")){
                //efetiva transacao
                status = iSev.obtemStatus(transacao.getId_tran());
                transacao.setStatus(status);
            }
        }
    }

    public void loadTransacoes(){
        File folder = new File("logs/");
        Transacao transacao;

        //somente arquivos de log (.txt)
        File [] listOfFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".log");
            }
        });

        //para cada log
        for (File file : listOfFiles) {
            if (file.isFile()) {
                //carrega a transacao pelo log
                transacao = new Transacao("logs/".concat(file.getName()));
                lista_transacao.add(transacao);
            }
        }
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
