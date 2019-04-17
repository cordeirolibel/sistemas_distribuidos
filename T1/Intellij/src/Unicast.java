package multicastpackage;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Queue;

/*
public class Unicast extends Thread{
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int meu_aid;
    int id_mestre;
    Processos processos;
    Relogio relogio;
    Queue<JSONObject> messages;
    int serverPort;

    public Unicast (int aserverPort, Processos aProcessos, Relogio arelogio, Queue <JSONObject> amessages, int aid){
        processos = aProcessos;
        relogio = arelogio;
        messages = amessages;
        serverPort = aserverPort;
        this.meu_aid = aid;

        System.out.println("AID: " + this.meu_aid);

        this.start();
    }

    public void run(){
        try{
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while(true) {
                Socket clientSocket = listenSocket.accept();
                unicastConnection c = new unicastConnection(clientSocket, processos, relogio, messages, this.meu_aid);
            }
        } catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
    }

}
*/

class Unicast extends Thread {
    DataInputStream in;
    DataOutputStream out;
    int meu_id;
    int id_mestre;
    Processos processos;
    Relogio relogio;
    Queue<JSONObject> messages;
    int serverPort;
    ServerSocket listenSocket;

    public Unicast (int aserverPort, Processos aProcessos, Relogio arelogio, Queue <JSONObject> amessages, int meu_aid) throws IOException {
        processos = aProcessos;
        relogio = arelogio;
        messages = amessages;
        meu_id = meu_aid;
        serverPort = aserverPort;

        System.out.println("MEU id FINAL> " + meu_aid);

        id_mestre = processos.procuraMestre();

        listenSocket = new ServerSocket(serverPort);

        this.start();

    }

    public void run(){
        while (true) {
            Socket clientSocket;
            try {

                clientSocket = listenSocket.accept();

                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                // Recebe resposta
                String data = in.readUTF();

                JSONObject jsonObj_rec = new JSONObject(data);
                String json_msg = jsonObj_rec.getString("json_msg");
                JSONObject jsonObj = new JSONObject(json_msg);

                meu_id = jsonObj.getInt("id");

                System.out.println("meuid: " + meu_id + " id mestre: " + id_mestre);
                if (meu_id == id_mestre) {
                    //Recebe tempo dos escravos

                    byte[] msg_signature = Base64.getDecoder().decode(jsonObj_rec.getString("signature"));


                    int id_slave = jsonObj.getInt("id");

                    // Pega chave publica do escravo
                    String pubkey_str = processos.get_pubKey(id_slave);
                    DSAPublicKey pubkey_slave = dsapack.DSA.Str2publicKey(pubkey_str);

                    boolean verif = dsapack.DSA.verify(pubkey_slave, json_msg.getBytes(), msg_signature);

                    if (verif) {
                        String msg = jsonObj.getString("msg");
                        System.out.println("msg if: " + msg);
                        if (msg.equals("Meu tempo")) {
                            System.out.println("meu tmepo: " + jsonObj.getLong("tempo"));
                            processos.salvaTempo(jsonObj.getLong("tempo"), id_slave);
                        }
                    } else {
                        System.out.printf("[!! %d] U fake bruh!\n", id_slave);
                    }
                } else {
                    // Slave recebe msg

                    // Abre mensagem
                    byte[] msg_signature = Base64.getDecoder().decode(jsonObj_rec.getString("signature"));

                    int id = jsonObj.getInt("id");

                    // Pega chave publica do suposto mestre
                    String pubkey_str = processos.get_pubKey(id);
                    DSAPublicKey pubkey_slave = dsapack.DSA.Str2publicKey(pubkey_str);

                    boolean verif = dsapack.DSA.verify(pubkey_slave, json_msg.getBytes(), msg_signature);

                    if (verif) {
                        String msg = jsonObj.getString("msg");
                        System.out.println("msg else: " + msg);

                        if (msg.equals("Ajuste")) {
                            long t = jsonObj.getLong("tempo");
                            relogio.ajustaTempo(t);
                            processos.salvaAjuste(t, meu_id);
                        } else if (msg.equals("Quero tempo")) {
                            // Prepara json resposta

                            JSONObject jsonObj_send = new JSONObject();
                            jsonObj_send.put("msg", "Meu tempo");
                            jsonObj_send.put("id", meu_id);
                            jsonObj_send.put("tempo", relogio.timePC());

                            messages.add(jsonObj_send);

                            //out.writeUTF(json_string);
                        }
                    } else {
                        System.out.printf("[!! %d] U fake bruh!\n", id);
                    }
                }

            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException e) {
                System.out.println("readline:" + e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (SignatureException e) {
                e.printStackTrace();
            }
        }
    }
}
