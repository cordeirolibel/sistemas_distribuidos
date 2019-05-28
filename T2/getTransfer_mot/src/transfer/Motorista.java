package transfer;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Motorista {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry refservicoNomes = LocateRegistry.getRegistry(1099);
        InterfaceServ refServidor = (InterfaceServ) refservicoNomes.lookup("servImpl");
        MotImpl motImpl = new MotImpl(refServidor);

        int menuScreen = 0;
        int i;

        int idMot = 8000;

        String input_msg = "Option: ";
        String[] setTransfer_msgs = {"Tipo Veículo: ", "Número máximo de passageiros: ", "Preço: "};

        String[] setTransfer_args = new String[3];

        int n_notific = 0;

        Oferta ofertaMot = new Oferta();
        LinkedList<Oferta> notificacoesOfertas = null;
        LinkedList<Oferta> notificacoesInteresse = null;

        while (true){
            if (menuScreen == 0){
                System.out.println("# ============== Menu Motorista TopTransfer.Net ============== #");
                System.out.println("1 - Cadastro transfer");
                System.out.println("2 - Notificações");
            }
            else if (menuScreen == 1){
                System.out.println("# ============== Menu Motorista TopTransfer.Net ============== #");

                for (i=0;i<setTransfer_msgs.length; i++){
                    // Cria oferta
                    Scanner keyboard = new Scanner(System.in);
                    System.out.printf(setTransfer_msgs[i]);
                    String keyboard_input = keyboard.nextLine();

                    setTransfer_args[i] = keyboard_input;
                }

                System.out.println(Arrays.toString(setTransfer_args));

                // Cria oferta
                ofertaMot.veiculo = setTransfer_args[0];
                ofertaMot.passageiros = Integer.parseInt(setTransfer_args[1]);
                ofertaMot.preco = Float.parseFloat(setTransfer_args[2]);
                ofertaMot.id = idMot;

                System.out.println("Registrar interesse? ");
                System.out.println("1. Sim \n 2. Não");

            }
            else if (menuScreen == 2){

                System.out.println("Sem notificações");
            }

            Scanner keyboard = new Scanner(System.in);
            System.out.printf(input_msg);
            String keyboard_input = keyboard.nextLine();

            System.out.println(keyboard_input);

            if (menuScreen == 0){
                if (keyboard_input.equals("1")){
                    menuScreen = 1;
                }
            }
            else if (menuScreen == 1){
                if (keyboard_input.equals("1")){
                    // Registra oferta no servidor
                    refServidor.cadastraOferta(ofertaMot, motImpl);

                    System.out.println("Oferta cadastrada com sucesso!");
                }
                else if (keyboard_input.equals("2")){
                    System.out.println("Cadastro de oferta cancelado");
                }
                else{
                    System.out.println("Opção inválida, cadastro de oferta cancelado");
                }

                menuScreen = 0;
            }

            //System.out.println(menuScreen);
        }
    }
}