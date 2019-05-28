package transfer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class MotImpl extends UnicastRemoteObject implements InterfaceMot{
    public int id;
    public int i=0;
    public InterfaceServ iSev;
    String[] setTransfer_msgs = {"Tipo Veículo: ", "Número máximo de passageiros: ", "Preço: "};
    String[] setTransfer_args = new String[3];

    protected MotImpl(InterfaceServ refServidor) throws RemoteException {
        System.out.println("MotImpl executado!");
        iSev = refServidor;
    }

    public void notificaReserva(Interesse interesse) throws RemoteException {
        System.out.println("Reserva efetuada com sucesso!");
        interesse.print();
    }

    public void notificaInteresse(Interesse interesse) throws RemoteException {
        System.out.println("Novo interesse!");
        System.out.printf("Interesse: ");
        interesse.print();

        System.out.println("Deseja fazer uma nova proposta?");
        System.out.println("1 - Sim // 2 - Não");

        Scanner keyboard = new Scanner(System.in);
        String keyboard_input = keyboard.nextLine();

        if (keyboard_input.equals("1")){
            Oferta ofertaMot = new Oferta();

            for (i=0;i<setTransfer_msgs.length; i++){
                // Cria oferta
                System.out.printf(setTransfer_msgs[i]);
                keyboard_input = keyboard.nextLine();

                setTransfer_args[i] = keyboard_input;
            }

            // Cria oferta
            ofertaMot.veiculo = setTransfer_args[0];
            ofertaMot.passageiros = Integer.parseInt(setTransfer_args[1]);
            ofertaMot.preco = Float.parseFloat(setTransfer_args[2]);

            System.out.println("Registrar oferta? ");
            System.out.println("1. Sim \n 2. Não");

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
}
