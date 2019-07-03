package transfer;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) throws IOException, NotBoundException {

        // Cria referencia com servidor
        Registry refservicoNomes = LocateRegistry.getRegistry(1099);
        InterfaceServ refServidor = (InterfaceServ) refservicoNomes.lookup("servImpl");
        CliImpl cliImpl = new CliImpl(refServidor);
        cliImpl.id = 42;

        //variaveis
        int idCarro = -1;
        boolean pula = false;
        int menuScreen = 0;
        int i;
        int useKeyboard = 1;

        String input_msg = "Option: ";
        String keyboard_input = "";

        refServidor.chamar("Ola servidor",cliImpl);

        while (true){
            //=======> MENU
            if (menuScreen == 0){
                // Tela principal
                System.out.println("# ============== Car Sharing UTFPR ============== #");
                System.out.println("1 - Ver carros disponiveis");
                System.out.printf("2 - Exit");
            }
            if (menuScreen == 1){
                // Tela consulta de veiculos disponiveis
                System.out.println("# ============== Car Sharing UTFPR ============== #");

                // Recebe resultado da consulta ao servidor de carros (SC)
                carros = refServidor.carrosLivres();

                int carrosLen = carros.size();

                // Print informações recebidas
                for (i = 0; i < carrosLen; i++) {
                    System.out.println("Carros");
                }

                if (carrosLen > 0) {
                    // Escolhe oferta pelo id dela
                    System.out.printf("Escolher numero do carro ou \'c\' para cancelar");

                    Scanner keyboard = new Scanner(System.in);
                    String keyboard_option = keyboard.nextLine();

                    // Print confirmação
                    if (keyboard_option.equals("c")) {
                        System.out.println("Operacao cancelada");
                    }
                    else {
                        idCarro = Integer.parseInt(carros[i].idCarro);
                        System.out.printf("Liberando veiculo " + carros.id + " ... \n");
                        useKeyboard = 0;
                        keyboard_input = "1";
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
                else{
                    menuScreen = 0;
                }
            }
            else if (menuScreen == 1){
                // Tela de liberação de veiculo
                if (keyboard_input.equals("1")){
                    // Efetua reserva

                    if (idCarro>=0){
                        // Efetua liberação do veiculo
                        if (refServidor.liberaCarro(idCarro)) {
                            System.out.println("Carro liberado com sucesso!");
                        }
                        else{
                            System.out.println("Erro ao processar a operação");
                        }
                    }
                }
                else{
                    System.out.println("Opção inválida, cadastro de interesse cancelado");
                }

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
