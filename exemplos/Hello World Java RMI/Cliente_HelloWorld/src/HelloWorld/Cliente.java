package HelloWorld;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Arrays;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) throws IOException {
        //Registry refservicoNomes = LocateRegistry.getRegistry(1099);
        //InterfaceServ refServidor = (InterfaceServ) refservicoNomes.lookup("servImpl");
        //CliImpl cliImpl = new CliImpl(refServidor);

        int menuScreen = 0;
        int i;
        String input_msg = "Option: ";
        String[] getTransfer_msgs = {"Origem: ", "Destino: ", "Dia: ", "Mês: ", "Hora: ", "Tipo Veículo: ", "Número de passageiros: ", "Preço: "};

        String[] getTransfer_args = new String[8];

        // Oferta ofertas;
        // Interesse interesseCli;

        while (true){
            if (menuScreen == 0){
                System.out.println("# ============== Menu client TopTransfer.Net ============== #");
                System.out.println("1 - Ver cotações para transfer");
                System.out.println("2 - Notificações");
            }
            else if (menuScreen == 1){
                System.out.println("# ============== Menu client TopTransfer.Net ============== #");

                for (i=0;i<getTransfer_msgs.length; i++){
                    // Recebe Interesse
                    Scanner keyboard = new Scanner(System.in);
                    System.out.printf(getTransfer_msgs[i]);
                    String keyboard_input = keyboard.nextLine();

                    getTransfer_args[i] = keyboard_input;
                }

                /*
                * // Cria interesse
                * int dia = Integer.parseInt(getTransfer_args[2]);
                * int mes = Integer.parseInt(getTransfer_args[3]);
                * int hora = Integer.parseInt(getTransfer_args[4]);
                * int n_passageiros = Integer.parseInt(getTransfer_args[5]);
                * float preco = Float.parseFloat(getTransfer_args[6];
                *
                * */

                //ofertas = cotacao(oferta);

                /*
                // Print lista de ofertas
                tamOfertas = ofertas.length;

                for (i=0; i<tamOfertas; i++){
                    System.out.printf("Oferta " + i + Arrays.toString(ofertas[i]));
                }

                // Prepara interesse
                // Ja tem veiculo, preço e data
                */

                System.out.printf("Escolher numero oferta ou 0 para cancelar: ");

                Scanner keyboard = new Scanner(System.in);
                int numero_oferta = keyboard.nextInt();

                /*
                * Efetua reserva
                * reserva(oferta[numero_oferta], interesseCli);
                * */

                System.out.printf("Interesse configurado: " + numero_oferta + " \n");
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
                    System.out.println("Interesse cadastrado com sucesso!");
                }
                else if (keyboard_input.equals("2")){
                    System.out.println("Cadastro interesse cancelado");
                }
                else{
                    System.out.println("Opção inválida, cadastro de interesse cancelado");
                }

                menuScreen = 0;
            }

            //System.out.println(menuScreen);
        }

    }
}
