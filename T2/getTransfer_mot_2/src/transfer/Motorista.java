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
        motImpl.id = idMot;

        String input_msg = "Option: ";
        String[] setTransfer_msgs = {"Tipo Veículo: ", "Número máximo de passageiros: ", "Preço: "};

        String[] setTransfer_args = new String[3];

        int n_notific = 0;

        Oferta ofertaMot = new Oferta();

        boolean pula = false;

        boolean cadastro_ou_altera = false;//false para cadastro
        while (true){
            if (menuScreen == 0){
                System.out.println("# ============== Menu Motorista TopTransfer.Net ============== #");
                if (cadastro_ou_altera==false)
                    System.out.println("1 - Cadastro oferta de transfer");
                else
                    System.out.println("1 - Alterar oferta de transfer");
            }
            else if (menuScreen == 1){
                System.out.println("# ============== Menu Motorista TopTransfer.Net ============== #");

                if (pula == false){
                    for (i=0;i<setTransfer_msgs.length; i++){
                        // Cria oferta
                        Scanner keyboard = new Scanner(System.in);
                        System.out.printf(setTransfer_msgs[i]);
                        String keyboard_input = keyboard.nextLine();

                        setTransfer_args[i] = keyboard_input;
                    }
                }

                System.out.println(Arrays.toString(setTransfer_args));

                // Cria oferta
                ofertaMot.veiculo = setTransfer_args[0];
                ofertaMot.passageiros = Integer.parseInt(setTransfer_args[1]);
                ofertaMot.preco = Float.parseFloat(setTransfer_args[2]);
                //ofertaMot.id = idMot;

                pula = false;

                System.out.println("Registrar oferta? ");
                System.out.println("1. Sim \n 2. Não");

            }
            else if (menuScreen == 2){
                System.out.println("# ============== Menu Motorista TopTransfer.Net ============== #");
                System.out.println(" -- Alterar Oferta -- ");

                for (i=0;i<setTransfer_msgs.length; i++){
                    // Cria oferta
                    Scanner keyboard = new Scanner(System.in);
                    System.out.printf(setTransfer_msgs[i]);
                    String keyboard_input = keyboard.nextLine();

                    setTransfer_args[i] = keyboard_input;
                }

                //System.out.println(Arrays.toString(setTransfer_args));

                // Cria oferta
                ofertaMot.veiculo = setTransfer_args[0];
                ofertaMot.passageiros = Integer.parseInt(setTransfer_args[1]);
                ofertaMot.preco = Float.parseFloat(setTransfer_args[2]);
                //ofertaMot.id = idMot;

                System.out.println("Confirmar alteração de oferta?");
                System.out.println("1. Sim \n 2. Não");
            }

            Scanner keyboard = new Scanner(System.in);
            System.out.printf(input_msg);
            String keyboard_input = keyboard.nextLine();

            System.out.printf("-key %s\n",keyboard_input);

            if (menuScreen == 0){
                if (keyboard_input.equals("1")){
                    if (cadastro_ou_altera==false)//cadastro
                        menuScreen = 1;
                    else
                        menuScreen = 2;
                }
                else if (keyboard_input.equals("3")){
                    menuScreen = 1;
                    setTransfer_args[0] = "Caminhonete";
                    setTransfer_args[1] = "3";
                    setTransfer_args[2] = "12.5";
                    pula = true;
                }
                else{
                    menuScreen = 0;
                }
            }
            else if (menuScreen == 1){
                if (keyboard_input.equals("1")){
                    // Registra oferta no servidor
                    refServidor.cadastraOferta(ofertaMot, motImpl);

                    cadastro_ou_altera = true;
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
            else if (menuScreen == 2){
                if (keyboard_input.equals("1")){
                    // Altera oferta no servidor
                    refServidor.alteraOferta(ofertaMot, motImpl);

                    System.out.println("Oferta alterada com sucesso!");
                }
                else if (keyboard_input.equals("2")){
                    System.out.println("Alteração de oferta cancelada");
                }
                else{
                    System.out.println("Opção inválida, alteração de oferta cancelada");
                }

                menuScreen = 0;
            }

            //System.out.println(menuScreen);
        }
    }
}