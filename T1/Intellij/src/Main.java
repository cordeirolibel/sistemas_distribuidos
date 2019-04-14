

import multicastpackage.MulticastPeer;

public class Main {

    public static void main(String[] args) {

        //String multicast_ip = "224.0.0.42";
        //int porta = 6789;

        //MulticastPeer processo1 = new MulticastPeer(multicast_ip,porta,1);
        //MulticastPeer processo2 = new MulticastPeer(multicast_ip,porta,2);
        //MulticastPeer processo3 = new MulticastPeer(multicast_ip,porta,3);

        //System.out.println("[main] Processos criados!!");

        DSA cripto = new DSA();
        String msg = "Oi teste";

        KeyPair keys = cripto.buildKeyPair();

        privKey = keys.getPrivate();
        pubKey = keys.getPublic();

        pubKey_str = cripto.publicKey2Str(pubKey);

        try {
            //deletar o processo 1 depois de 10 segundos
            //Thread.sleep(10000);
            //processo1.desativa();
            //System.out.println("[main] Processo 1 deletado");
            System.out.println("pubKey: ", pubKey_str);

            //int segundos=0;
            ////infinite loop
            //while(true){
            //    System.out.printf("[main] %3d segundos\n", segundos);
            //    Thread.sleep(1000);
            //    segundos+=1;
            //}


        }catch (InterruptedException e){System.out.println("Interrupt: " + e.getMessage());}


    }

}