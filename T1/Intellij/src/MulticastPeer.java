
package multicastpackage;

// download: http://www.java2s.com/Code/Jar/o/Downloadorgjsonjar.htm
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class MulticastPeer{

    static Queue <JSONObject> messages;
    MulticastSocket s;
    InetAddress group;
    int porta;
    int id;

    public MulticastPeer(String ip_grupo, int aporta)  {

        System.out.println(ip_grupo);
        System.out.println(aporta);

        porta = aporta;

        //fila de mensagens
        messages  = new LinkedList<>();

        // Pegar IP da maquina
        //InetAddress address = InetAddress.getLocalHost();
        //String hostIP = address.getHostAddress() ;
        //String hostName = address.getHostName();
        //System.out.println( "IP: " + hostIP + "\n" + "Name: " + hostName);

        try {
            s = new MulticastSocket(porta);

            //entra no grupo
            group = InetAddress.getByName(ip_grupo);
            s.joinGroup(group);

            // Cria threads
            // thread le as mensagens e salva numa fila
            Connection threadOuvinte = new Connection(s,messages);
            // thread envia mensagens de tempos em tempos
            EstouVivo threadEstouVivo = new EstouVivo(s,group,porta,5000);
            threadEstouVivo.ativa();

            //salva o id de todos e cria o seu
            id = criaId();

            JSONObject jsonRecebido;
            while(true){

                //Se tiver uma mensagem na fila de mensagens
                if (messages.size()>0){
                    jsonRecebido = messages.remove();
                    System.out.println("Recebido lendo da fila: " + jsonRecebido.getString("msg"));
                }

                Thread.sleep(1);

                //text= keyboard.nextLine();

            }
            //s.leaveGroup(group);
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }catch(JSONException e) {System.out.println("Json:"+e.getMessage());
        }catch (InterruptedException e){System.out.println("Interrupt: " + e.getMessage());
        }finally {if(s != null) s.close();}
    }

    //envia mensagem
    public void enviaMensagem(JSONObject msg) {

        try {
            //json -> string -> bytes -> datagramPacket
            String json_string = msg.toString();
            byte[] m = json_string.getBytes();
            DatagramPacket messageOut = new DatagramPacket(m, m.length, group, porta);
            s.send(messageOut);
        }catch (IOException e){System.out.println("IO: " + e.getMessage());}

    }

    //envia mensagem
    public void enviaMensagem(String msg, int id) {

        try {
            //cria e envia e envia mensagem de sou novo
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("msg", "novo no grupo");
            jsonObj.put("id", 0);
            enviaMensagem(jsonObj);
        }catch(JSONException e) {System.out.println("Json:"+e.getMessage());}
    }

    //salva o ip de todos e cria o proprio
    public int criaId() {
        try{
            enviaMensagem("Novo no Grupo",0);
            //espera 1 segundo
            Thread.sleep(1000);

        }catch (InterruptedException e){System.out.println("Interrupt: " + e.getMessage());}

        return 1;
    }

}

