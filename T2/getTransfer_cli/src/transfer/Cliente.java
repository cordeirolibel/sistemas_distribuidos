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
        Registry refservicoNomes = LocateRegistry.getRegistry(1099);
        InterfaceServ refServidor = (InterfaceServ) refservicoNomes.lookup("servImpl");
        CliImpl cliImpl = new CliImpl(refServidor);
        cliImpl.id = 42;

        int numero_oferta = -1;
        boolean pula = false;
        int menuScreen = 0;
        int i;
        String input_msg = "Option: ";
        String keyboard_input = "";
        String[] getTransfer_msgs = {"Origem: ", "Destino: ", "Dia: ", "Mês: ", "Hora: ", "Tipo Veículo: ", "Número de passageiros: ", "Preço: "};

        String[] getTransfer_args = new String[8];

        LinkedList<Oferta> ofertas = null;
        Interesse interesseCli = new Interesse();

        refServidor.chamar("Ola servidor",cliImpl);

        while (true){
            keyboard_input = "";
            if (menuScreen == 0){
                System.out.println("# ============== Menu client TopTransfer.Net ============== #");
                System.out.println("1 - Ver cotações para transfer");
                System.out.println("2 - Notificações");
            }
            if (menuScreen == 1){
                System.out.println("# ============== Menu client TopTransfer.Net ============== #");

                if (pula==false) {
                    for (i = 0; i < getTransfer_msgs.length; i++) {
                        // Recebe Interesse
                        Scanner keyboard = new Scanner(System.in);
                        System.out.printf(getTransfer_msgs[i]);
                        keyboard_input = keyboard.nextLine();
                        getTransfer_args[i] = keyboard_input;
                    }
                    keyboard_input = "";

                }


                // Cria interesse
                interesseCli.itinerario = getTransfer_args[0] + " - " + getTransfer_args[1];
                interesseCli.dia = Integer.parseInt(getTransfer_args[2]);
                interesseCli.mes = Integer.parseInt(getTransfer_args[3]);
                interesseCli.hora = Integer.parseInt(getTransfer_args[4]);
                interesseCli.veiculo = getTransfer_args[5];
                interesseCli.n_passageiros = Integer.parseInt(getTransfer_args[6]);
                interesseCli.preco = Float.parseFloat(getTransfer_args[7]);
                interesseCli.print();
                pula = false;

                // Recebe ofertas de acordo com o interesse do cliente
                ofertas = refServidor.cotacao(interesseCli);

                // Print lista de ofertas
                int tamOfertas = ofertas.size();

                // VERIFICAR O PRINT DAS OFERTAS
                System.out.printf("============================================\n");
                for (i=0; i<tamOfertas; i++){
                    System.out.printf("Oferta [" + i + "] " );
                    ofertas.get(i).print();
                }

                if (tamOfertas>0) {
                    // Escolhe oferta pelo id dela
                    System.out.printf("Escolher numero oferta ou \'c\' para cancelar: ");

                    Scanner keyboard = new Scanner(System.in);
                    numero_oferta = keyboard.nextInt();

                    // Print confirmação
                    if (keyboard_input.equals("c")) {
                        System.out.println("Registrar interesse? ");
                        System.out.println("1. Sim \n2. Não");
                        numero_oferta = -1;
                    }
                    else {
                        System.out.printf("Interesse configurado: " + numero_oferta + " \n");
                    }
                }
                else{
                    System.out.println(" ");
                }
            }
            else if (menuScreen == 2){
                keyboard_input = "3";
            }

            if (! keyboard_input.equals("3")) {
                Scanner keyboard = new Scanner(System.in);
                System.out.printf(input_msg);
                keyboard_input = keyboard.nextLine();

                System.out.println(keyboard_input);
            }

            if (menuScreen == 0){
                if (keyboard_input.equals("1")){
                    menuScreen = 1;
                }
                else if (keyboard_input.equals("3")){
                    menuScreen = 1;
                    getTransfer_args[0] = "Curitiba";
                    getTransfer_args[1] = "Caioba";
                    getTransfer_args[2] = "30";
                    getTransfer_args[3] = "05";
                    getTransfer_args[4] = "11";
                    getTransfer_args[5] = "Caminhonete";
                    getTransfer_args[6] = "3";
                    getTransfer_args[7] = "12.5";
                    pula = true;
                }
            }
            else if (menuScreen == 1){
                // TELA DE RESERVA DE OFERTA
                if (keyboard_input.equals("1")){
                    // Efetua reserva (E SE NÃO TIVER OFERTA DISPONIVEL ENVIA OFERTA NULL??)

                    if (numero_oferta>=0 ) {
                        if (refServidor.reserva(ofertas.get(numero_oferta), interesseCli)) {
                            //System.out.println("Interesse cadastrado com sucesso!");
                            System.out.println("Reserva cadastrada com sucesso!");
                        }
                        else{
                            System.out.println("Motorista ocupado nessa data e horario");
                        }
                    }
                }
                else if (keyboard_input.equals("2")){
                    refServidor.registraInteresseCli(interesseCli,  cliImpl);
                    System.out.println("Interesse cadastrado com sucesso!");
                    //System.out.println("Cadastro interesse cancelado");
                }
                else if (keyboard_input.equals("3")){
                    System.out.println("Não obteve nenhuma cotação!");
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
