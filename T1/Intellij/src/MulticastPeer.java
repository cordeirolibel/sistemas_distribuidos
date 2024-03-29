package multicastpackage;

// download: http://www.java2s.com/Code/Jar/o/Downloadorgjsonjar.htm
//           https://jar-download.com/maven-repository-class-search.php?search_box=com.sun.org.apache.xerces.internal.impl.dv.util.Base64
//           https://search.maven.org/artifact/com.google.code.gson/gson/2.8.5/jar
//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.codec.binary.Base64;
//import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import dsapack.DSA;


public class MulticastPeer extends Thread {

    private static final int TIME_KEEP_ALIVE = 5000;//ms
    private static final int TIME_AJUSTE = 7000;//ms

    Queue<JSONObject> messages;
    Connection threadOuvinte;
    Unicast threadUnicast;
    KeepAlive threadKeepAlive;
    Processos processos;
    Relogio clk;
    MulticastSocket s;
    InetAddress group;
    int porta;
    int porta_processo;
    int meu_id;
    int master_id;
    String pubKey_str;
    DSAPrivateKey p_privKey;
    String ip_unicast;


    public MulticastPeer(String ip_grupo, int aporta, int aid, long ajuste_manual) throws GeneralSecurityException {

        meu_id = aid;
        master_id = 0;
        porta = aporta;
        porta_processo = aporta + meu_id;
        //porta = porta_processo;

        //fila de mensagens
        messages = new LinkedList<>();

        //pegando o ip da maquina
        try {
            ip_unicast = InetAddress.getByName("localhost").getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // Gerador de par de chaves
        KeyPair kp = DSA.buildKeyPair();

        // Chave privada e publica
        p_privKey = (DSAPrivateKey) kp.getPrivate();
        DSAPublicKey my_pubKey = (DSAPublicKey) kp.getPublic();

        // Converte chave publica para string
        pubKey_str = DSA.publicKey2Str(my_pubKey);

        try {
            s = new MulticastSocket(porta);

            //entra no grupo
            group = InetAddress.getByName(ip_grupo);
            s.joinGroup(group);

            System.out.printf("[^^ %d] Fui criado no ip %s e porta %d\n", meu_id, ip_grupo, porta_processo);
            System.out.printf("[^^ %d] Fui criado com pubKey: ", meu_id);
            System.out.println(pubKey_str);

            // Cria threads
            // thread le as mensagens e salva numa fila
            threadOuvinte = new Connection(s, messages);
            // thread envia mensagens de tempos em tempos
            threadKeepAlive = new KeepAlive(s, group, porta, meu_id, TIME_KEEP_ALIVE);

            processos = new Processos();
            //salva o id do mestre e salva todos os processos
            salvaProcessosDoGrupo();

            threadOuvinte.salva_id(meu_id);

            clk = new Relogio(processos,ajuste_manual);

            threadUnicast = new Unicast(porta_processo, processos, clk, messages, meu_id, master_id);

            //s.leaveGroup(group);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }

        //Thread
        this.start();
    }

    public void run() {

        JSONObject jsonRecebido;
        String msg;
        int id;
        int keep_alive_time = 0;
        int ajusta_relogio_time = 0;

        try {
            while (true) {
                //Se tiver uma mensagem na fila de mensagens
                if (messages.size() > 0) {
                    jsonRecebido = messages.remove();
                    msg = jsonRecebido.getString("msg");
                    id = jsonRecebido.getInt("id");
                    if ( !msg.equals("Meu tempo")) {
                        System.out.printf("[>> %d] Recebido: %s de id: %d\n", meu_id, msg, id);
                    }

                    //novo processo no grupo
                    switch (msg) {
                        case "Novo no Grupo":

                            // Cria json para responder ao novo processo
                            JSONObject jsonObj = new JSONObject();
                            jsonObj.put("msg", "Seja bem vindo!");
                            jsonObj.put("pubKey", pubKey_str);
                            jsonObj.put("id", meu_id);
                            jsonObj.put("porta", porta_processo);

                            // Envia json de resposta ao novo processo
                            enviaMensagem(jsonObj);

                            // Cria processo na lista
                            processos.add(id, jsonRecebido.getString("pubKey"), jsonRecebido.getInt("porta"));

                            if (atualizaMestre()) {
                                keep_alive_time = 0;
                            }
                            break;
                        case "Meu Relogio":
                            processos.salvaTempo(jsonRecebido.getLong("time"), id);
                            break;
                        case "Seja bem vindo!":
                            break;
                        case "Meu tempo":
                            int porta_mestre = processos.getPortaId(master_id);
                            unicastClient(ip_unicast, id, porta_mestre, "Meu tempo", jsonRecebido.getLong("tempo"), p_privKey);
                        case "Estou Vivo!":
                            keep_alive_time = 0;
                            break;
                        default:
                            System.out.printf("Mensagem nao eh valida: %s %d\n", msg, id);
                    }

                }

                keep_alive_time += 1;
                //se não recebeu o keep alive do mestre
                if ((keep_alive_time >= TIME_KEEP_ALIVE * 1.1) && (master_id != meu_id)) { //10% de tolerancia
                    processos.delete(master_id);
                    atualizaMestre();
                    keep_alive_time = 0;
                }

                //se sou o mestre
                if(meu_id == master_id){
                    //se chegou a hora de atualizar os relogios
                    if (ajusta_relogio_time >= TIME_AJUSTE){

                        //salvo meu tempo
                        long t_mestre = clk.timePC();
                        processos.salvaTempo(t_mestre, meu_id);
                        processos.salva_trec(clk.timePC(), meu_id);
                        processos.salva_tzero_id(clk.timePC(), meu_id);

                        int slave_porta;
                        //envia mensagem unicast para todos [pedindo os tempos]
                        for(int i=0;i<processos.size();i++){
                            slave_porta = processos.getPorta(i);
                            if ((slave_porta != 0) && (porta_processo != slave_porta)){
                                processos.salva_tzero(clk.timePC(), i);
                                unicastClient (ip_unicast, meu_id,slave_porta, "Quero tempo", 0, p_privKey);
                            }
                        }

                        //espera 1 segundo
                        Thread.sleep(1000);

                        //roda o algoritmo Berkeley
                        processos.Berkeley(meu_id);

                        //print da tabela
                        processos.print();

                        //envia mensagem unicast para todos mandando os ajustes
                        for(int i=0;i<processos.size();i++){
                            slave_porta = processos.getPorta(i);
                            if ((slave_porta != 0) && (porta_processo != slave_porta)){
                                unicastClient (ip_unicast,meu_id, slave_porta, "Ajuste", processos.get_ajuste_interador(i), p_privKey);
                            }
                        }

                        ajusta_relogio_time = 0;
                    }
                    else{
                        ajusta_relogio_time += 1;
                    }

                }
                else{
                    ajusta_relogio_time = 0;
                }

                Thread.sleep(1);
            }
        } catch (JSONException e) {
            System.out.println("Json:" + e.getMessage());
        } catch (InterruptedException ex) {
            Logger.getLogger(MulticastPeer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //} catch (UnknownHostException ex) {
        //    Logger.getLogger(MulticastPeer.class.getName()).log(Level.SEVERE, null, ex);
        //}

    }

    public void desativa() {
        threadKeepAlive.desativa();
        threadKeepAlive.stop();
        threadOuvinte.stop();
        threadUnicast.stop();
        this.stop();
    }

    //envia mensagem com JSON
    public void enviaMensagem(JSONObject msg) {
        try {
            //json -> string -> bytes -> datagramPacket
            String json_string = msg.toString();
            byte[] m = json_string.getBytes();
            DatagramPacket messageOut = new DatagramPacket(m, m.length, group, porta);
            s.send(messageOut);

            System.out.printf("[<< %d] Enviando: %s \n", meu_id, msg.getString("msg"));
        } catch (JSONException e) {
            System.out.println("Json:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }

    }

    //envia mensagem Msg + id
    public void enviaMensagem(String msg, int id) {

        try {
            //cria e envia e envia mensagem
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("msg", msg);
            jsonObj.put("id", id);
            enviaMensagem(jsonObj);
        } catch (JSONException e) {
            System.out.println("Json:" + e.getMessage());
        }
    }

    //salva o id do mestre e salva todos os processos
    public void salvaProcessosDoGrupo() {

        //envia uma mensagem e espera a resposta do grupo
        try {
            //enviaMensagem("Novo no Grupo",meu_id);

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("msg", "Novo no Grupo");
            jsonObj.put("pubKey", pubKey_str);
            jsonObj.put("id", meu_id);
            jsonObj.put("porta", porta_processo);

            enviaMensagem(jsonObj);

            //espera 1 segundo
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            System.out.println("Interrupt: " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //me adiciono
        processos.add(meu_id, pubKey_str, porta_processo);

        //le mensagem do grupo e salva cada um
        try {
            // Para todas as mensagens recebidas
            JSONObject jsonRecebido;

            while (messages.size() > 0) {
                jsonRecebido = messages.remove();

                //mensagens validas
                if (jsonRecebido.getString("msg").equals("Seja bem vindo!")) {
                    int processo_id = jsonRecebido.getInt("id");
                    String processo_pubKey = jsonRecebido.getString("pubKey");
                    int nova_porta_processo = jsonRecebido.getInt("porta");
                    processos.add(processo_id, processo_pubKey, nova_porta_processo);
                }
            }
        } catch (JSONException e) {
            System.out.println("Json:" + e.getMessage());
        }

        atualizaMestre();
    }

    //retorna o id atualizado do mestre
    //retorna true se mudou o mestre
    private boolean atualizaMestre() {

        int novo_mestre = processos.procuraMestre();
        try{
            threadUnicast.mudaMestre(novo_mestre);
        }
        catch (Exception e){}


        if ((meu_id == novo_mestre) && (novo_mestre != master_id)) {
            threadKeepAlive.ativa();
            System.out.printf("[** %d] Eu sou o mestre!\n", meu_id);
        } else if ((meu_id == master_id) && (novo_mestre != meu_id)) {
            threadKeepAlive.desativa();
            System.out.printf("[** %d] Não sou mais o mestre! é o %d\n", meu_id, novo_mestre);
        } else if (novo_mestre != master_id) {
            System.out.printf("[** %d] Novo Mestre! é o %d\n", meu_id, novo_mestre);
        }

        //atualiza o mestre
        if (master_id != novo_mestre) {
            master_id = novo_mestre;
            return true;
        }
        return false;
    }

    public void unicastClient(String ip, int idThread, int process_port, String msg, long dado_tempo, DSAPrivateKey privKey) {

        System.out.printf("[<- %d] Envia unicast para ip/porta: %s/%d, msg: %s, tempo: %d\n",meu_id,ip,process_port,msg,dado_tempo);

        Socket socket = null;

        try {
            socket = new Socket(ip, process_port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // Cria json para enviar para o slave
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", meu_id);
            jsonObj.put("tempo", dado_tempo);
            jsonObj.put("msg", msg);
            //json -> string
            String json_string = jsonObj.toString();

            // Assina mensagem
            byte[] signature = DSA.sign(privKey, json_string.getBytes());
            String s_signature = new String(Base64.encodeBase64(signature));
            jsonObj.put("sig",s_signature);

            //jsonObj
            JSONObject json_send = new JSONObject();
            json_send.put("json_msg", json_string);
            json_send.put("signature", s_signature);

            //Enviar json
            out.writeUTF(json_send.toString());

            socket.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
