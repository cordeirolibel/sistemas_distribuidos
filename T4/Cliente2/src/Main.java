package tranCar;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Logger;

class Cliente {
    public static void main(String[] args) throws IOException, NotBoundException {

        // Cria referencia com servidor
        Registry refservicoNomes = LocateRegistry.getRegistry(1099);
        InterfaceSevCarro refServidor = (InterfaceSevCarro) refservicoNomes.lookup("servImpl");
        CliImpl cliImpl = new CliImpl(refServidor);
        cliImpl.id = 42;

        //variaveis
        int idCarro_lib = -1;
        boolean pula = false;
        int menuScreen = 0;
        int i;
        int useKeyboard = 1;

        Logger logger;

        // Cria lista de carros
        LinkedList<Carro> lista_carros = new LinkedList<Carro>();

        String input_msg = "Option: ";
        String keyboard_input = "";

        refServidor.chamar("Ola servidor", cliImpl);

        while (true){
            //=======> MENU
            if (menuScreen == 0){
                // Tela principal
                System.out.println("# ============== Car Sharing UTFPR ============== #");
                System.out.println("1 - Ver carros disponiveis");
                System.out.println("2 - Exit");
            }
            if (menuScreen == 1){
                // Tela consulta de veiculos disponiveis
                System.out.println("# ============== Car Sharing UTFPR ============== #");

                // Recebe resultado da consulta ao servidor de carros (SC)
                lista_carros = refServidor.carrosLivres();

                int carrosLen = lista_carros.size();

                // Print informações recebidas
                for (i = 0; i < carrosLen; i++) {
                    lista_carros.get(i).carInfo();
                }

                if (carrosLen > 0) {
                    // Escolhe oferta pelo id dela
                    System.out.println("Escolher numero do carro ou \'c\' para cancelar");

                    Scanner keyboard = new Scanner(System.in);
                    String keyboard_option = keyboard.nextLine();

                    // Print confirmação
                    if (keyboard_option.equals("c")) {
                        System.out.println("Operacao cancelada");
                    }
                    else {
                        idCarro_lib = Integer.parseInt(keyboard_option);
                        System.out.printf("Liberando veiculo " + lista_carros.get(idCarro_lib-1).id_carro + " ... \n");
                        useKeyboard = 0;
                        keyboard_input = "1";

                        // Libera veiculo
                        cliImpl.id_tran = refServidor.liberaCarro(idCarro_lib, cliImpl.id, cliImpl);
                        cliImpl.status_tran = 1; //0 - Finalizada 1 - Executando

                        System.out.printf("Executando transação [id: %s]\n", cliImpl.id_tran);

                        // Salva no log o status final da transação


                        cliImpl.id_tran = 0;
                        cliImpl.status_tran = 0;
                    }
                }
                else{
                    System.out.println("Nenhum veiculo disponivel perto de você :( ");
                }
            }
            else if (menuScreen==2){
                break;
            }

            //===================================================================
            //          Leitura teclado
            if (useKeyboard == 1) {
                Scanner keyboard = new Scanner(System.in);
                System.out.printf(input_msg);
                keyboard_input = keyboard.nextLine();
            }

            if (menuScreen == 0){
                if (keyboard_input.equals("1")) {
                    menuScreen = 1;
                }
                else if (keyboard_input.equals("2")){
                    break;
                }
                else{
                    menuScreen = 0;
                }
            }
            else if (menuScreen == 1){
                menuScreen = 0;
            }
            else if( menuScreen == 2){
                menuScreen = 0;
            }

            useKeyboard = 1;
            //System.out.println(menuScreen);
        }

    }
}
