package tranCar;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;

public class SevImpl extends UnicastRemoteObject implements InterfaceSevCarro {

    LinkedList<Carro> lista_carros;
    LinkedList<Transacao> lista_transacao;
    LinkedList<InterfaceCli> lista_clieImpl;
    InterfaceBanco bancoImpl;

    static float VALOR_CARRO = 12;

    public SevImpl() throws RemoteException {
        System.out.println("SevImpl executando!");

        //cria todas as listas e filas
        lista_carros = new LinkedList<Carro>();
        lista_transacao = new LinkedList<Transacao>();
        lista_clieImpl = new LinkedList<InterfaceCli>();


        //carrega arquivos locais
        //loadListas();
        initCarros();
        saveListas();

        loadTransacoes();

    }

    @Override
    public int liberaCarro(int id_carro, int id_clie, InterfaceCli clieImpl) throws RemoteException {
        System.out.printf("=======Libera Carro========\n");
        System.out.printf("Carro: %d e Cliente: %d\n",id_carro,id_clie);

        //busca o carro com id_carro
        Carro carro = buscaCarro(id_carro);

        //abrindo transacao
        Transacao transacao = abreTransacao(carro,clieImpl);

        return transacao.getId_tran();
    }

    @Override
    public LinkedList<Carro> carrosLivres() throws RemoteException {
        System.out.printf("=======Carros Livres========\n");

        LinkedList<Carro> lista_carros_retorno = new LinkedList<Carro>();
        Carro carro;
        int size = lista_carros.size();

        //procura os carros livres
        for (int i=0;i<size;i++){
            carro = lista_carros.get(i);
            //se carro esta livre
            if (carro.isLivre()){
                //adiciona na lista de retorno
                lista_carros_retorno.add(carro);
            }
        }

        System.out.printf("Retornando lista de %d carros livres.\n",lista_carros_retorno.size());
        return lista_carros_retorno;
    }

    @Override
    public void ref_banco(InterfaceBanco bancoImpl) throws RemoteException {
        this.bancoImpl = bancoImpl;
    }

    //retorna o estado da transacao
    //efetivada, cancelada ou provisoria.
    public String obtemStatus(int id_tran) throws RemoteException{
        Transacao transacao = new Transacao();
        return transacao.getStatus();
    }

// --------------------------------------------------------------
// --------------------------------------------------------------
//                    Internas
// --------------------------------------------------------------
// --------------------------------------------------------------

    //verifica se tem alguma transacao pendente e atualiza
    public void atualizaTransacoes() throws RemoteException {
        int size = lista_transacao.size();
        Transacao transacao;

        //procura os transacoes em aberto
        for (int i=0;i<size;i++) {
            transacao = lista_transacao.get(i);
            if (transacao.getStatus().equals("provisoria")){
                //efetiva transacao
                obterDecisao(transacao.getId_tran());
            }
        }
    }


    private void obterDecisao(int id_tran){
        /*
        Após ter votado “sim”, mas ainda não ter recebido uma
        resposta (efetivar/abortar), o participante solicita a decisão
        ao coordenador.
        */


        //busca transacao, carro e interface do cliente
        Transacao transacao = buscaTransacao(id_tran);
        Carro carro = buscaCarro(transacao.getId_recurso());
        InterfaceCli clieImpl = buscaCliente(transacao.getId_clie());

        //  VOTO 1: Cordenador
        if (transacao.getVoto("coordenador").equals("") && !transacao.estaAbortada()) {
            // verifica se carro esta livre
            if (!carro.isLivre()) {
                //se o carro nao esta livre
                transacao.aborta();
            }
        }

        //  VOTO 2: Banco
        if (transacao.getVoto("banco").equals("") && !transacao.estaAbortada()) {
            boolean voto_banco;
            try {
                voto_banco = bancoImpl.debitarValor(clieImpl.id, VALOR_CARRO, id_tran);
            } catch (RemoteException e) {
                voto_banco = false;
                e.printStackTrace();
            }
            if (voto_banco == false) {
                transacao.aborta();
            }
        }


        //um nao, cancela tudo
        if(transacao.estaAbortada()){
            abortaTransacao(id_tran);

            //retorno para o banco e cliete
            try {
                //avisa o banco
                bancoImpl.aborta(id_tran);

                //avisa o cliente
                clieImpl.aborta(id_tran);

            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
        else{
            //efetiva transacao
            terminaTransacao(id_tran);

            //retorno para o banco e cliente
            try {
                //avisa o banco
                bancoImpl.efetiva(id_tran);

                //avisa o cliente
                clieImpl.efetiva(id_tran);

            } catch (RemoteException e) {
                e.printStackTrace();
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

    //procura na lista_carros o carro com id_carro
    public InterfaceCli buscaCliente(int id_clie){

        int size = lista_clieImpl.size();
        InterfaceCli clieImpl;

        //procura as transacoes na lista
        for (int i=0;i<size;i++) {
            clieImpl = lista_clieImpl.get(i);
            if (clieImpl.id == id_clie){
                return clieImpl;
            }
        }

        return null;
    }

    // ----------------------
    //  Transacoes
    // ----------------------



    //cria uma transacao e adiciona a lista de transacoes
    //retorna o id da transacao
    Transacao abreTransacao(Carro carro, InterfaceCli clieImpl){

        //cria transacao
        Transacao transacao = new Transacao();
        transacao.setId_recurso(carro.getId_carro());

        transacao.setId_clie(clieImpl.id);
        lista_transacao.add(transacao);
        lista_clieImpl.add(clieImpl);
        saveListas();

        //trava carro (timeout 0.1s)
        if (!carro.bloqueiaRecurso()){
            //aborta se nao conseguir travar o recurso
            transacao.aborta();
        }
        saveListas();

        System.out.printf(" => Aberto transacao %d \n",transacao.getId_tran());
        return transacao;
    }

    //finaliza a transacao, liberando os recursos
    public void terminaTransacao(int id_tran){

        //busca transacao e carro
        Transacao transacao = buscaTransacao(id_tran);
        Carro carro = buscaCarro(transacao.getId_recurso());

        //efetiva transacao
        //Alugo o carro
        carro.carroAlugado();

        //libera o recurso
        carro.liberaRecurso();

        System.out.printf(" => transacao %d efetivada\n",transacao.getId_tran());

        //remove a transacao
        lista_transacao.remove(transacao);

        saveListas();
    }

    public void abortaTransacao(int id_tran){

        //busca transacao e carro
        Transacao transacao = buscaTransacao(id_tran);
        Carro carro = buscaCarro(transacao.getId_recurso());

        //libera o recurso
        carro.liberaRecurso();

        System.out.printf(" => transacao %d abortada\n",transacao.getId_tran());

        //remove a transacao
        lista_transacao.remove(transacao);
    }

    //procura na lista_carros o carro com id_carro
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

    //printa todas as transacoes
    public void printaTransacoes(){

        int size = lista_transacao.size();
        Transacao transacao;

        //procura as transacoes na lista
        for (int i=0;i<size;i++) {
            transacao = lista_transacao.get(i);
            transacao.print();
        }
    }

    // ----------------------
    //  Auxiliares Carro
    // ----------------------

    //procura na lista_carros o carro com id_carro
    Carro buscaCarro(int id_carro){

        int size = lista_carros.size();
        Carro carro;

        //procura os carro na lista
        for (int i=0;i<size;i++) {
            carro = lista_carros.get(i);
            if (carro.getId_carro() == id_carro){
                return carro;
            }
        }

        return null;
    }

    //cria uns 3 carros disponiveis no servidor
    void initCarros(){

        lista_carros.add(new Carro(1,"BMW"));
        lista_carros.add(new Carro(2,"Fusca"));
        lista_carros.add(new Carro(3,"Duster"));

    }

    // ----------------------
    //  listas
    // ----------------------

    //salva listas em um arquivo local
    private void saveListas() {
        try {
            FileOutputStream f = new FileOutputStream(new File("myObjects.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(lista_carros);
            o.writeObject(lista_clieImpl);

            //close
            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //carrega listas de um arquivo local
    private void loadListas() {
        try {
            File f = new File("myObjects.txt");
            //se arquivo existir
            if(f.exists() && !f.isDirectory()) {
                FileInputStream fi = new FileInputStream(f);
                ObjectInputStream oi = new ObjectInputStream(fi);

                // Read objects
                lista_carros = (LinkedList<Carro>) oi.readObject();
                lista_clieImpl = (LinkedList<InterfaceCli>) oi.readObject();

                //close
                oi.close();
                fi.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void chamar(String msg, InterfaceCli iCli) throws RemoteException {
        System.out.printf("Mensagem recebida do cliente: %s\n",msg);
        iCli.echo(msg);
    }

}
