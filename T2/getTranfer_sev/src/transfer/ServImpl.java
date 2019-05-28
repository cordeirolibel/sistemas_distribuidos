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
    LinkedList<Horarios> lista_horarios_mot;

    public ServImpl() throws RemoteException {
        System.out.printf("Hello de ServImpl\n");
        
        lista_interesses_clie  = new LinkedList<Interesse>();
        lista_interfaces_clie  = new LinkedList<InterfaceCli>();

        lista_oferta           = new LinkedList<Oferta>();
        lista_interfaces_mot   = new LinkedList<InterfaceMot>();

        lista_horarios_mot     = new LinkedList<Horarios>();
    }

    // --------------------------------------------------------------
    // =====> Notificacao
    // --------------------------------------------------------------

    //cliente recebe notificações do servidor quando
    //um veículo de seu interesse tiver seu preço reduzido para os dados
    //de data e hora informados

    //para um cliente especifico
    //OK
    private void notificaCliente(Oferta oferta, int id_cli,Horarios horarios) throws RemoteException {
        System.out.printf("=======Notifica Cliente========\n");
        System.out.printf("==> Oferta de motorista %d para cliente %d\n",oferta.id,id_cli);
        oferta.print();

        InterfaceCli iClie_i = lista_interfaces_clie.get(id_cli);
        Interesse interesse_i = lista_interesses_clie.get(id_cli);

        if (comparaInteresseComOferta(interesse_i,oferta,horarios)){
            iClie_i.notificaOferta(oferta);
            System.out.printf("    Interesse cadastrado\n");
        }
    }

    //verfica todos os clientes
    //OK
    private void notificaClientes(Oferta oferta, Horarios horarios) throws RemoteException {

        int size = lista_interesses_clie.size();

        for (int i=0;i<size;i++) {
            notificaCliente(oferta, i,horarios);
        }

    }

    //verfica todos os motoristas
    //OK
    private void notificaMotoristas(Interesse interesse)throws RemoteException{
        System.out.printf("=======Notifica Motoristas========\n");
        System.out.printf("==> Interrese de cliente %d\n",interesse.id);
        interesse.print();

        InterfaceMot iMot_i;
        Oferta oferta_i;
        Horarios horarios;
        int size = lista_oferta.size();

        for (int i=0;i<size;i++) {
            oferta_i = lista_oferta.get(i);
            horarios = lista_horarios_mot.get(i);
            if (comparaInteresseComOferta(interesse,oferta_i,horarios)){
                iMot_i = lista_interfaces_mot.get(i);
                iMot_i.notificaInteresse(interesse);
                System.out.printf("    Notifica motorista %d\n",oferta_i.id);
            }
        }
    }

    // --------------------------------------------------------------
    // =====> Remote
    // --------------------------------------------------------------

    @Override
    public void chamar(String msg, InterfaceCli iCli) throws RemoteException {
        System.out.printf("Mensagem recebida do cliente: %s\n",msg);
        iCli.echo(msg);
    }

    @Override //OK
    public LinkedList<Oferta> cotacao(Interesse interesse) throws RemoteException {
        System.out.printf("=======Cotacao========\n");
        System.out.printf("==> Interesse de %d:\n",interesse.id);
        interesse.print();

        Oferta oferta_i;
        Horarios horarios;
        int size = lista_oferta.size();
        LinkedList<Oferta> lista_oferta_retorno = new LinkedList<Oferta>();

        //procura o oferta desse motorista
        for (int i=0;i<size;i++){
            oferta_i = lista_oferta.get(i);
            horarios = lista_horarios_mot.get(i);
            if (comparaInteresseComOferta(interesse,oferta_i,horarios)){
                lista_oferta_retorno.add(oferta_i);
            }
        }

        return lista_oferta_retorno;
    }

    @Override //falta notificacao e controle de concorrencia
    public boolean reserva(Oferta oferta, Interesse interesse) throws RemoteException {
        System.out.printf("=======Reserva========\n");
        System.out.printf("==> Oferta de motorista %d e cliente %d\n",oferta.id,interesse.id);
        oferta.print();

        Oferta oferta_i;
        InterfaceMot iMot_i;
        Horarios horarios;
        int size = lista_oferta.size();
        //procura o oferta desse motorista
        for (int i=0;i<size;i++){
            oferta_i = lista_oferta.get(i);
            iMot_i = lista_interfaces_mot.get(i);
            if (oferta_i.id == oferta.id) {
                horarios = lista_horarios_mot.get(i);
                //controle de concorrencia
                synchronized (this) {
                    if(horarios.disponivel(interesse.dia, interesse.mes, interesse.hora)) {
                        horarios.set(interesse.dia, interesse.mes, interesse.hora);
                    }
                    else{
                        System.out.printf("\tMotorista ocupado\n");
                        return false;
                    }
                }
                //notifica o motorista que a reserva foi feita
                System.out.printf("\tReserva efetuada\n");
                iMot_i.notificaReserva(interesse);
                return true;
            }
        }
        System.out.printf("\tMotorista nao encontrado\n");
        return false;
    }

    @Override //OK
    public void registraInteresseCli(Interesse interesse, InterfaceCli iCli) throws RemoteException {
        System.out.printf("=======Registra Interesse de Cliente ========\n");
        System.out.printf("===> Interesse de cliente %d\n",interesse.id);
        interesse.print();

        int size = lista_interesses_clie.size();
        interesse.id = size;
        lista_interesses_clie.add(interesse);
        lista_interfaces_clie.add(iCli);
        notificaMotoristas(interesse);
    }


    @Override //OK
    // Efetua o cadastro de uma oferta feita pelo motorista
    public void cadastraOferta(Oferta oferta, InterfaceMot iMot) throws RemoteException {
        System.out.printf("=======Cadastra Oferta========\n");
        System.out.printf("===> Ofera de motorista %d\n",oferta.id);
        oferta.print();

        int size = lista_oferta.size();
        Horarios horarios = new Horarios();
        oferta.id = size;
        lista_oferta.add(oferta);
        lista_interfaces_mot.add(iMot);
        lista_horarios_mot.add(horarios);
        notificaClientes(oferta,horarios);
    }

    //quando o motorista manda uma proposta para um cliente especifico
    @Override //OK
    public void novaProposta(Oferta oferta, Interesse interesse) throws RemoteException {
        System.out.printf("=======Nova Proposta========\n");
        System.out.printf("==> Oferta de motorista %d para cliente %d\n",oferta.id,interesse.id);
        oferta.print();

        Oferta oferta_i;
        int id_cli = interesse.id;
        int id_mot = -1;

        int size = lista_oferta.size();
        //procura o oferta desse motorista
        /*
        for (int i=0;i<size;i++) {
            oferta_i = lista_oferta.get(i);
            if (oferta_i.id == oferta.id) {
                id_mot = i;
            }
        }
        if (id_mot == -1){ return; }
        */
        id_mot = oferta.id;

        //notifica o cliente dessa nova proposta
        Horarios horarios = lista_horarios_mot.get(id_mot);
        notificaCliente(oferta, id_cli,horarios);
    }

    @Override //OK
    public void alteraOferta(Oferta oferta, InterfaceMot iMot) throws RemoteException {
        System.out.printf("=======Altera Oferta========\n");
        System.out.printf("===> Ofera de motorista %d\n",oferta.id);
        oferta.print();

        InterfaceMot iMot_i;
        Horarios horarios = null;
        int size = lista_oferta.size();
        //procura o oferta desse motorista
        for (int i=0;i<size;i++){
            iMot_i = lista_interfaces_mot.get(i);
            horarios = lista_horarios_mot.get(i);

            if (iMot_i.id == iMot.id){ //mesmo motorista
                //remove oferta
                lista_oferta.remove(i);
                lista_interfaces_mot.remove(i);
                lista_horarios_mot.remove(i);
                break;
            }
        }

        //add oferta
        size = lista_oferta.size();
        oferta.id = size;
        lista_oferta.add(oferta);
        lista_interfaces_mot.add(iMot);
        lista_horarios_mot.add(horarios);
        notificaClientes(oferta,horarios);
    }


    // --------------------------------------------------------------
    // =====> Internas
    // --------------------------------------------------------------
    
    //retorna true se oferta o1 atende oferta o2
    /*private boolean comparaOfertas(Oferta o1,Oferta o2){
        if ((o1.itinerario.equals(o2.itinerario)) &
                (o1.veiculo.equals(o2.veiculo)) &
                (o1.passageiros == o2.passageiros) &
                (o1.data.compareTo(o2.data)==0) &    // ====== ver melhor comparacao de datas
                (o1.preco <= o2.preco)&
                (o1.ativa==1)){
            return true;
        }
        return false;
    }*/

    //retorna true se oferta atende Interesse
    private boolean comparaInteresseComOferta(Interesse i,Oferta o,Horarios h){
        if ((o.veiculo.equals(i.veiculo)) &
            (h.disponivel(i.dia,i.mes,i.hora)) &
            (o.preco <= i.preco)){
            return true;
        }
        return false;
    }


}
