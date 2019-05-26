package transfer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.LinkedList;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ{
    LinkedList<Interesse> lista_interesses_clie;
    LinkedList<InterfaceCli> lista_interfaces_clie;
    LinkedList<Oferta> lista_oferta;
    LinkedList<InterfaceMot> lista_interfaces_mot;

    public ServImpl() throws RemoteException {
        lista_interesses_clie  = new LinkedList<Interesse>();
        lista_interfaces_clie  = new LinkedList<InterfaceCli>();

        lista_oferta           = new LinkedList<Oferta>();
        lista_interfaces_mot   = new LinkedList<InterfaceMot>();
    }

    // --------------------------------------------------------------
    // =====> Notificacao
    // --------------------------------------------------------------

    //cliente recebe notificações do servidor quando
    //um veículo de seu interesse tiver seu preço reduzido para os dados
    //de data e hora informados

    //para um cliente especifico
    //OK
    private void notificaCliente(Oferta oferta, int id_cli) throws RemoteException {

        InterfaceCli iClie_i = lista_interfaces_clie.get(id_cli);
        Interesse interesse_i = lista_interesses_clie.get(id_cli);

        if (comparaInteresseComOferta(interesse_i,oferta)){
            iClie_i.notificaOferta(oferta);
        }

    }

    //verfica todos os clientes
    //OK
    private void notificaClientes(Oferta oferta) throws RemoteException {

        int size = lista_interesses_clie.size();

        for (int i=0;i<size;i++) {
            notificaCliente(oferta, i);
        }

    }

    //verfica todos os motoristas
    //OK
    private void notificaMotoristas(Interesse interesse)throws RemoteException{

        InterfaceMot iMot_i;
        Oferta oferta_i;
        int size = lista_oferta.size();

        for (int i=0;i<size;i++) {
            oferta_i = lista_oferta.get(i);
            if (comparaInteresseComOferta(interesse,oferta_i)){
                iMot_i = lista_interfaces_mot.get(i);
                iMot_i.notificaInteresse(interesse);
            }
        }

    }


    // --------------------------------------------------------------
    // =====> Remote
    // --------------------------------------------------------------

    @Override
    public void chamar(String msg, InterfaceCli iCli) throws RemoteException {

    }

    @Override  //falta so verificar a comparacao de datas
    public LinkedList<Oferta> cotacao(Oferta oferta) throws RemoteException {
        Oferta oferta_i;
        int size = lista_oferta.size();
        LinkedList<Oferta> lista_oferta_retorno = new LinkedList<Oferta>();

        //procura o oferta desse motorista
        for (int i=0;i<size;i++){
            oferta_i = lista_oferta.get(i);
            if (comparaOfertas(oferta_i,oferta)){
                lista_oferta_retorno.add(oferta_i); // ###############TODO: ver melhor comparacao de datas
            }
        }

        return lista_oferta_retorno;
    }

    @Override //falta notificacao e controle de concorrencia
    public void reserva(Oferta oferta) throws RemoteException {
        Oferta oferta_i;
        int size = lista_oferta.size();
        //procura o oferta desse motorista
        for (int i=0;i<size;i++){
            oferta_i = lista_oferta.get(i);
            if (oferta_i.id == oferta.id) {
                //#################TODO: notifica o motorista? e controle de concorrencia
                oferta.ativa = 0;
                break;
            }
        }
    }

    @Override //OK
    public void registraInteresseCli(Interesse interesse, InterfaceCli iCli) throws RemoteException {
        int size = lista_interesses_clie.size();
        interesse.id = size;
        lista_interesses_clie.add(interesse);
        lista_interfaces_clie.add(iCli);
        notificaMotoristas(interesse);
    }


    @Override //OK
    public void cadastraOferta(Oferta oferta, InterfaceMot iMot) throws RemoteException {
        int size = lista_oferta.size();
        oferta.id = size;
        lista_oferta.add(oferta);
        lista_interfaces_mot.add(iMot);
        notificaClientes(oferta);
    }

    //quando o motorista manda uma proposta para um cliente especifico
    @Override //OK
    public void novaProposta(Oferta oferta, Interesse interesse) throws RemoteException {
        int id = interesse.id;
        notificaCliente(oferta, id);

    }

    @Override //OK
    public void alteraOferta(Oferta oferta, InterfaceMot iMot) throws RemoteException {
        InterfaceMot iMot_i;
        int size = lista_oferta.size();
        //procura o oferta desse motorista
        for (int i=0;i<size;i++){
            iMot_i = lista_interfaces_mot.get(i);

            if (iMot_i.id == iMot.id){ //mesmo motorista
                //remove oferta
                lista_oferta.remove(i);
                lista_interfaces_mot.remove(i);
                break;
            }
        }

        //add oferta
        size = lista_oferta.size();
        oferta.id = size;
        lista_oferta.add(oferta);
        lista_interfaces_mot.add(iMot);
        notificaClientes(oferta);
    }


    // --------------------------------------------------------------
    // =====> Internas
    // --------------------------------------------------------------
    
    //retorna true se oferta o1 atende oferta o2
    private boolean comparaOfertas(Oferta o1,Oferta o2){
        if ((o1.itinerario.equals(o2.itinerario)) &
                (o1.veiculo.equals(o2.veiculo)) &
                (o1.passageiros == o2.passageiros) &
                (o1.data.compareTo(o2.data)==0) &    // ###############TODO: ver melhor comparacao de datas
                (o1.preco <= o2.preco)&
                (o1.ativa==1)){
            return true;
        }
        return false;
    }

    //retorna true se oferta atende Interesse
    private boolean comparaInteresseComOferta(Interesse i,Oferta o){
        if ((o.veiculo.equals(i.veiculo)) &
            (i.data.compareTo(o.data)==0) &    // ###############TODO: ver melhor comparacao de datas
            (o.preco <= i.preco)&
            (o.ativa==1)){
            return true;
        }
        return false;
    }


}
