
package multicastpackage;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.io.IOException;

//Thread para leitura do Multicast
class KeepAlive extends Thread  {
    MulticastSocket multiSocket;
    DatagramPacket messageOut;
    int tempo_ms;
    boolean ligado;

    public KeepAlive (MulticastSocket amultiSocket, InetAddress group, int porta, int atempo_ms) {
        multiSocket = amultiSocket;
        tempo_ms = atempo_ms;
        ligado = false;

        try{
            //cria mensagem de envio
            //json -> string -> bytes -> datagramPacket
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("msg", "Estou Vivo!");
            String msg = jsonObj.toString();
            byte[] m = msg.getBytes();
            messageOut = new DatagramPacket(m, m.length, group, porta);
        }catch(JSONException e) {System.out.println("Json:"+e.getMessage());}

        //Thread
        this.start();
    }

    public void ativa(){
        ligado = true;
    }

    public void desativa(){
        ligado = false;
    }

    public void run()  {
        //envia mensagem de estou vivo
        try {
            while(true) {
                if(ligado) {
                    multiSocket.send(messageOut);
                }
                Thread.sleep(tempo_ms);
            }
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }catch (InterruptedException e){System.out.println("Interrupt: " + e.getMessage());}

    }
}
