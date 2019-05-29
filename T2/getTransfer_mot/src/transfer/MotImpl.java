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



        System.out.println();
        System.out.println(" =======Notificacao==========");
        System.out.println(" ====== Novo interesse! ========");
        System.out.printf("Interesse: ");
        interesse.print();

        System.out.println("Deseja fazer uma nova proposta?");
        System.out.println("1 - Sim // 2 - Não");

        Scanner keyboard = new Scanner(System.in);
        String keyboard_input = keyboard.nextLine();

        if (keyboard_input.equals("1")){
            Oferta ofertaMot = new Oferta();

            ofertaMot.passageiros = interesse.n_passageiros;
            ofertaMot.veiculo = interesse.veiculo;

            /**for (i=0;i<setTransfer_msgs.length; i++){
                // Cria oferta
                System.out.printf(setTransfer_msgs[i]);
                keyboard_input = keyboard.nextLine();

                setTransfer_args[i] = keyboard_input;
            }

            // Cria oferta
            ofertaMot.veiculo = setTransfer_args[0];
            ofertaMot.passageiros = Integer.parseInt(setTransfer_args[1]);
            ofertaMot.preco = Float.parseFloat(setTransfer_args[2]);**/

            System.out.println("Novo preco: ");
            keyboard = new Scanner(System.in);
            keyboard_input = keyboard.nextLine();
            ofertaMot.preco = Float.parseFloat(keyboard_input);

            System.out.println("Registrar oferta? ");
            System.out.println("1. Sim \n 2. Não");
            //Scanner key = new Scanner(System.in);
            System.out.println("Option: ");
            keyboard_input = keyboard.nextLine();

            if (keyboard_input.equals("1")){
                // Registra oferta no servidor
                iSev.novaProposta(ofertaMot, interesse);

                System.out.println("Nova proposta cadastrada com sucesso!");
            }
            else {
                System.out.println("Cadastro de nova proposta cancelado");
            }
        }
        else{
            System.out.println("Cadastro de nova proposta cancelado");
        }
}
