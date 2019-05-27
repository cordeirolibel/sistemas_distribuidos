package transfer;

import java.util.Scanner;

public class Motorista {
    public static void main(String[] args) {
        System.out.println("Hello Motorista!");

        int menuScreen = 0;
        int i;

        String input_msg = "Option: ";
        String[] setTransfer_msgs = {"Tipo Veículo: ", "Número máximo de passageiros: ", "Preço: "};

        // Oferta ofertas;

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

                    setTransfer_msgs[i] = keyboard_input;
                }

                /*
                 * // Cria oferta
                 * int n_passageiros = Integer.parseInt(getTransfer_args[1]);
                 * float preco = Float.parseFloat(getTransfer_args[2];
                 *
                 * */

                /*

                // Registra oferta no servidor

                */
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
                    // Envia para sv
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