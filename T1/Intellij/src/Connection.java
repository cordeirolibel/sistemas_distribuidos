
package multicastpackage;


import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.Queue;

//Thread para leitura do Multicast
class Connection extends Thread {
    MulticastSocket multiSocket;
    Queue<JSONObject> messages;

    public Connection (MulticastSocket amultiSocket, Queue <JSONObject> amessages) {
        multiSocket = amultiSocket;
        messages = amessages;
        this.start();
    }

    public void run()  {
        try {			                 // an echo server
            byte[] buffer = new byte[1000];
            while(true){
                //leitura
                DatagramPacket in = new DatagramPacket(buffer, buffer.length);
                multiSocket.receive(in);

                //salva na fila de mensagens como um json
                //bytes -> String -> json
                JSONObject jsonObj = new JSONObject(new String(in.getData()));
                messages.add(jsonObj);

                System.out.println("Recebido e gravado na fila: " + jsonObj.getString("msg"));
            }
        }catch(IOException e) {System.out.println("readline:"+e.getMessage());
        }catch(JSONException e) {System.out.println("readline:"+e.getMessage());};
    }
}
