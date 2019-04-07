
package multicastpackage;

// download: http://www.java2s.com/Code/Jar/o/Downloadorgjsonjar.htm
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class MulticastPeer extends Thread {

    Queue <JSONObject> messages;
    LinkedList<Processo> processos;
    Connection threadOuvinte;
    KeepAlive threadKeepAlive;
    MulticastSocket s;
    InetAddress group;
    int porta;
    int meu_id;

    public MulticastPeer(String ip_grupo, int aporta) {

        System.out.println(ip_grupo);
        System.out.println(aporta);
        meu_id = 0;
        porta = aporta;

        //fila de mensagens
        messages  = new LinkedList<>();

        //Lista de processos
        processos = new LinkedList<Processo>();

        try {
            s = new MulticastSocket(porta);

            //entra no grupo
            group = InetAddress.getByName(ip_grupo);
            s.joinGroup(group);

            // Cria threads
            // thread le as mensagens e salva numa fila
            threadOuvinte = new Connection(s,messages);
            // thread envia mensagens de tempos em tempos
            threadKeepAlive = new KeepAlive(s,group,porta,5000);
            //threadKeepAlive.ativa();

            //salva o id de todos e cria o seu
            meu_id = criaId();
            System.out.printf("Criado Id: %d\n",meu_id);
            threadOuvinte.salva_id(meu_id);

            //s.leaveGroup(group);
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());}

        //Thread
        this.start();
    }


    public void run()  {

        JSONObject jsonRecebido;
        try {
            while (true) {

                //Se tiver uma mensagem na fila de mensagens
                if (messages.size() > 0) {
                    jsonRecebido = messages.remove();
                    System.out.printf(" >> (id: %d) Recebido: %s de id: %d\n",meu_id,jsonRecebido.getString("msg"),jsonRecebido.getInt("id"));

                    //novo processo no grupo
                    if (jsonRecebido.getString("msg").equals("Novo no Grupo")) {
                        enviaMensagem("Seja bem vindo!", meu_id);
                    }
                }

                Thread.sleep(1);

            }
        }catch(JSONException e) {System.out.println("Json:"+e.getMessage());
        }catch (InterruptedException e){System.out.println("Interrupt: " + e.getMessage());}

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
            jsonObj.put("msg", msg);
            jsonObj.put("id", id);
            enviaMensagem(jsonObj);
        }catch(JSONException e) {System.out.println("Json:"+e.getMessage());}
    }

    //salva o ip de todos e cria o proprio
    public int criaId() {

        //envia uma mensagem e espera a resposta do grupo
        try{
            enviaMensagem("Novo no Grupo",0);
            //espera 1 segundo
            Thread.sleep(1000);

        }catch (InterruptedException e){System.out.println("Interrupt: " + e.getMessage());}


        //le mensagem do grupo e salva cada um
        int maior_id = 0;
        try{
            // Para todas as mensagens recebidas
            JSONObject jsonRecebido;

            while (messages.size()>0){
                jsonRecebido = messages.remove();

                //mensagens validas
                if(jsonRecebido.getString("msg").equals("Seja bem vindo!")){
                    int processo_id = jsonRecebido.getInt("id");
                    processos.add(new Processo(processo_id));

                    maior_id = maior_id>processo_id ? maior_id : processo_id;
                }
            }
        }catch(JSONException e) {System.out.println("Json:"+e.getMessage());}

        return maior_id+1;
    }

}

