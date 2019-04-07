
package multicastpackage;


import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.Queue;

//Thread para leitura do Multicast
class Connection extends Thread  {
    MulticastSocket multiSocket;
    Queue<JSONObject> messages;
    int meu_id;

    public Connection (MulticastSocket amultiSocket, Queue <JSONObject> amessages) {
        multiSocket = amultiSocket;
        messages = amessages;
        meu_id = 0;

        //Thread
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
                if (jsonObj.getInt("id")==meu_id) {//desconsidera as proprias mensagens
                    continue;
                }
                messages.add(jsonObj);
                //System.out.printf(" >> [id: %d] Recebido e gravado na fila: %s\n",meu_id,jsonObj.getString("msg"));

            }
        }catch(IOException e) {System.out.println("readline:"+e.getMessage());
        }catch(JSONException e) {System.out.println("json:"+e.getMessage());}
    }

    public void salva_id(int id){
        meu_id = id;
    }
}
