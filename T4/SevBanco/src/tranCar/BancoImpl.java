package tranCar;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class BancoImpl extends UnicastRemoteObject implements InterfaceBanco {

    LinkedList<Cliente> lista_clientes;
    LinkedList<Transacao> lista_transacao;

    protected BancoImpl(InterfaceSevCarro refServidor) throws RemoteException {

        lista_transacao = new LinkedList<Transacao>();
        lista_clientes = new LinkedList<Cliente>();

        refServidor.ref_banco(this);

        //loadListas();
        initClientes();

        saveListas();
        loadTransacoes();
    }


    @Override
    public boolean debitarValor(int id_clie, float valor, int id_trans) throws RemoteException {

        Cliente cliente = buscaCliente(id_clie);

        //inicia transacao
        Transacao transacao = new Transacao(id_trans,valor);
        transacao.setId_recurso(id_clie);
        lista_transacao.add(transacao);

        //trava carro (timeout 0.1s)
        if (!cliente.bloqueiaRecurso()){
            //aborta se nao conseguir travar o recurso
            transacao.aborta();
        }
        saveListas();

        if (!transacao.estaAbortada()) {
            if (!cliente.temSaldo(valor)) {
                //aborta se nao tiver saldo
                transacao.aborta();
            }
        }

        //cancela tudo
        if(transacao.estaAbortada()){
            System.out.println("Return voto nao");
            return false; //voto  nao

        }
        else{
            System.out.println("Return voto sim");
            return true; //voto  sim
        }

    }

    @Override
    public void efetiva(int id_tran) throws RemoteException {

        //busca transacao e Cliente
        Transacao transacao = buscaTransacao(id_tran);
        Cliente cliente = buscaCliente(transacao.getId_recurso());

        transacao.setStatus("efetivada");
        cliente.debitaValor(transacao.getValor());
        cliente.liberaRecurso();
        saveListas();

        System.out.printf("Transacao %d de cliente %d cancelada\n",id_tran,cliente.id_clie);
        System.out.printf("Saldo %.2f\n",cliente.saldo);

    }

    @Override
    public void aborta(int id_tran) throws RemoteException {

        //busca transacao e Cliente
        Transacao transacao = buscaTransacao(id_tran);
        Cliente cliente = buscaCliente(transacao.getId_recurso());

        //efetiva transacao e libera recurso
        transacao.setStatus("cancelada");
        cliente.liberaRecurso();
        saveListas();

        System.out.printf("Transacao %d de cliente %d cancelada\n",id_tran,cliente.id_clie);
        System.out.printf("Saldo %.2f\n",cliente.saldo);
    }

    //==============================
    // Internas

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


    //procura na lista_clientes o cliente com id_cliente
    Cliente buscaCliente(int id_clie){

        int size = lista_clientes.size();
        Cliente cliente;

        //procura os carro na lista
        for (int i=0;i<size;i++) {
            cliente = lista_clientes.get(i);
            if (cliente.getId_clie() == id_clie){
                return cliente;
            }
        }

        return null;
    }
    //salva listas em um arquivo local
    private void saveListas() {
        try {
            FileOutputStream f = new FileOutputStream(new File("myObjects.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(lista_clientes);

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
                lista_clientes = (LinkedList<Cliente>) oi.readObject();

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

    //cria uns 3 clientes disponiveis no servidor
    //cada um tem 20 reais
    void initClientes(){

        lista_clientes.add(new Cliente(42,20));
        lista_clientes.add(new Cliente(43,20));
        lista_clientes.add(new Cliente(0,20));

    }

}
