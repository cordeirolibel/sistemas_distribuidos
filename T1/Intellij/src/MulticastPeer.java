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

    private static final int TIME_KEEP_ALIVE = 5000;//ms

    Queue <JSONObject> messages;
    Connection threadOuvinte;
    KeepAlive threadKeepAlive;
    Processos processos;
    MulticastSocket s;
    InetAddress group;
    int porta;
    int meu_id;
    int master_id;

    public MulticastPeer(String ip_grupo, int aporta, int aid) {



        meu_id = aid;
        master_id = 0;
        porta = aporta;

        //fila de mensagens
        messages  = new LinkedList<>();

        try {
            s = new MulticastSocket(porta);

            //entra no grupo
            group = InetAddress.getByName(ip_grupo);
            s.joinGroup(group);

            System.out.printf("[^^ %d] Fui criado no ip %s e porta %d\n",aid,ip_grupo,aporta);

            // Cria threads
            // thread le as mensagens e salva numa fila
            threadOuvinte = new Connection(s,messages);
            // thread envia mensagens de tempos em tempos
            threadKeepAlive = new KeepAlive(s,group,porta,meu_id,TIME_KEEP_ALIVE);

            processos = new Processos();
            //salva o id do mestre e salva todos os processos
            salvaProcessosDoGrupo();

            threadOuvinte.salva_id(meu_id);

            //s.leaveGroup(group);
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());}

        //Thread
        this.start();
    }


    public void run()  {

        JSONObject jsonRecebido;
        String msg;
        int id;
        int keep_alive_time = 0;

        try {
            while (true) {

                //Se tiver uma mensagem na fila de mensagens
                if (messages.size() > 0) {
                    jsonRecebido = messages.remove();
                    msg = jsonRecebido.getString("msg");
                    id = jsonRecebido.getInt("id");
                    System.out.printf("[>> %d] Recebido: %s de id: %d\n",meu_id,msg,id);

                    //novo processo no grupo
                    switch(msg){
                        case "Novo no Grupo":
                            enviaMensagem("Seja bem vindo!", meu_id);
                            processos.add(id);
                            if (atualizaMestre()){
                                keep_alive_time = 0;
                            }
                            break;
                        case "Meu Relogio":
                            processos.salvaTempo(jsonRecebido.getLong("time"),id);
                            break;
                        case "Seja bem vindo!":
                            break;
                        case "Estou Vivo!":
                            keep_alive_time = 0;
                            break;
                        default:
                            System.out.printf("Mensagem nao eh valida: %s %d\n",msg,id);
                    }

                }

                keep_alive_time += 1;
                //se não recebeu o keep alive do mestre
                if ((keep_alive_time >=TIME_KEEP_ALIVE*1.1) && (master_id!=meu_id)){ //10% de tolerancia
                    processos.delete(master_id);
                    atualizaMestre();
                    keep_alive_time = 0;
                }

                Thread.sleep(1);
            }
        }catch(JSONException e) {System.out.println("Json:"+e.getMessage());
        }catch (InterruptedException e){System.out.println("Interrupt: " + e.getMessage());}

    }
    public void desativa(){
        threadKeepAlive.desativa();
    }

    //envia mensagem
    public void enviaMensagem(JSONObject msg) {
        try {
            //json -> string -> bytes -> datagramPacket
            String json_string = msg.toString();
            byte[] m = json_string.getBytes();
            DatagramPacket messageOut = new DatagramPacket(m, m.length, group, porta);
            s.send(messageOut);

            System.out.printf("[<< %d] Enviando: %s \n",meu_id,msg.getString("msg"));
        }catch(JSONException e) {System.out.println("Json:"+e.getMessage());
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

    //salva o id do mestre e salva todos os processos
    public void salvaProcessosDoGrupo() {

        //envia uma mensagem e espera a resposta do grupo
        try{
            enviaMensagem("Novo no Grupo",meu_id);
            //espera 1 segundo
            Thread.sleep(1000);

        }catch (InterruptedException e){System.out.println("Interrupt: " + e.getMessage());}


        //me adiciono
        processos.add(meu_id);

        //le mensagem do grupo e salva cada um
        try{
            // Para todas as mensagens recebidas
            JSONObject jsonRecebido;

            while (messages.size()>0){
                jsonRecebido = messages.remove();

                //mensagens validas
                if(jsonRecebido.getString("msg").equals("Seja bem vindo!")){
                    int processo_id = jsonRecebido.getInt("id");
                    processos.add(processo_id);
                }
            }
        }catch(JSONException e) {System.out.println("Json:"+e.getMessage());}


        atualizaMestre();
    }

    //retorna o id atualizado do mestre
    //retorna true se mudou o mestre
    private boolean atualizaMestre(){

        int novo_mestre = processos.procuraMestre();

        if ((meu_id==novo_mestre)&&(novo_mestre != master_id)){
            threadKeepAlive.ativa();
            System.out.printf("[** %d] Eu sou o mestre!\n",meu_id);
        }
        else if((meu_id==master_id)&&(novo_mestre!=meu_id)){
            threadKeepAlive.desativa();
            System.out.printf("[** %d] Não sou mais o mestre! é o %d\n",meu_id,novo_mestre);
        }
        else if(novo_mestre!=master_id) {
            System.out.printf("[** %d] Novo Mestre! é o %d\n",meu_id,novo_mestre);
        }

        //atualiza o mestre
        if (master_id != novo_mestre){
            master_id = novo_mestre;
            return true;
        }
        return false;
    }
}

