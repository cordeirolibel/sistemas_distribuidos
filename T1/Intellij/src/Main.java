

import multicastpackage.MulticastPeer;

public class Main {

    public static void main(String[] args) {

        String multicast_ip = "224.0.0.42";
        int porta = 6789;

        MulticastPeer processo1 = new MulticastPeer(multicast_ip,porta);
        MulticastPeer processo2 = new MulticastPeer(multicast_ip,porta);
        MulticastPeer processo3 = new MulticastPeer(multicast_ip,porta);

        System.out.println("Processos criados!!");
        while(true);

    }

}