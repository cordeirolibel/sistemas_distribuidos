package tranCar;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;

public class SevImpl extends UnicastRemoteObject implements InterfaceSevCarro {

    LinkedList<Carro> lista_carros;

    public SevImpl() throws RemoteException {
        System.out.println("SevImpl executando!");

        //cria todas as listas e filas
        lista_carros = new LinkedList<Carro>();
        initCarros();
    }

    @Override
    public boolean liberaCarro(int id_carro, int id_clie) throws RemoteException {
        System.out.printf("=======Libera Carro========\n");
        System.out.printf("Carro: %d e Cliente: %d\n",id_carro,id_clie);

        //busca o carro com id_carro
        Carro carro = buscaCarro(id_carro);

        //trava carro
        if (!carro.lock()){
            //aborta se nao conseguir travar o recurso
            return false;
        }

        //verifica se carro esta livre
        if (!carro.isLivre()){
            //libera recurso e aborta
            carro.unlock();
            return false;
        }

        carro.carroAlugado();

        return true;
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
        return lista_carros_retorno;
    }

// --------------------------------------------------------------
// =====> Internas
// --------------------------------------------------------------

    //procura na lista_carros o carro com id_carro
    Carro buscaCarro(int id_carro){

        int size = lista_carros.size();
        Carro carro;

        //procura os carro na lista
        for (int i=0;i<size;i++) {
            carro = lista_carros.get(i);
            if (carro.id_carro == id_carro){
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

    @Override
    public void chamar(String msg, InterfaceCli iCli) throws RemoteException {
        System.out.printf("Mensagem recebida do cliente: %s\n",msg);
        iCli.echo(msg);
    }

}
