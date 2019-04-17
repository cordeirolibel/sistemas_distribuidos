import multicastpackage.MulticastPeer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;

public class Main {

    public static void main(String[] args) throws GeneralSecurityException, UnknownHostException {

        String multicast_ip = "224.0.0.42";

        int porta = 6788;

        MulticastPeer processo1 = new MulticastPeer(multicast_ip,porta,1,0);
        MulticastPeer processo2 = new MulticastPeer(multicast_ip,porta,2,0);
        MulticastPeer processo3 = new MulticastPeer(multicast_ip,porta,3,0);
        MulticastPeer processo4 = new MulticastPeer(multicast_ip,porta,3,0);

        System.out.println("[main] Processos criados!!");

        try {
            //deletar o processo 1 depois de 10 segundos
            //Thread.sleep(10000);
            //processo1.desativa();
            //System.out.println("[main] Processo 1 deletado");

            int segundos=0;
            //infinite loop
            while(true){
                System.out.printf("[main] %3d segundos\n", segundos);
                Thread.sleep(1000);
                segundos+=1;
            }


        }catch (InterruptedException e){System.out.println("Interrupt: " + e.getMessage());}


    }

}