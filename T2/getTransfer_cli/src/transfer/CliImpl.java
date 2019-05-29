package transfer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli{
    public int id;
    InterfaceServ iSev;

    public CliImpl(InterfaceServ refServidor) throws RemoteException {
        System.out.println("CliImpl executado!");
        iSev = refServidor;
    }


    @Override
    public void echo(String msg) throws RemoteException {
        System.out.printf("Echo enviado para o servidor: %s\n",msg);
    }

    @Override
    public void notificaOferta(Oferta oferta,Interesse interesse) throws RemoteException {
        System.out.println("Pressione Enter");
        System.out.printf("===============================================\n");
        System.out.printf("=============  Notificacao de Oferta   ========\n");
        interesse.print();
        System.out.println("\n1. Aceitar \n2. Não aceitar");

        System.out.printf(">> Opção: \n");

        Scanner keyboard_impl = new Scanner(System.in);
        String input = keyboard_impl.nextLine();

        if(input.equals("1")){
            System.out.println("Resposta servidor: ");
            if (iSev.reserva(oferta,interesse)){
                System.out.printf("Reserva Efetuada\n");
            }
            else{
                System.out.printf("Motorista nao disponivel\n");
            }
        }

    }
}
