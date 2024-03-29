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
    Socket clientSocket;

    public Unicast (int aserverPort, Processos aProcessos, Relogio arelogio, Queue <JSONObject> amessages, int meu_aid, int aid_mestre) throws IOException {
        processos = aProcessos;
        relogio = arelogio;
        messages = amessages;
        meu_id = meu_aid;
        serverPort = aserverPort;

        //id_mestre = processos.procuraMestre();
        id_mestre = aid_mestre;

        listenSocket = new ServerSocket(serverPort);


        this.start();

    }

    public void mudaMestre(int aid_mestre){
        id_mestre = aid_mestre;
    }

    public void run(){
        int id_receb;
        while (true) {

            try {
                clientSocket = listenSocket.accept();

                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                // Recebe resposta
                String data = in.readUTF();

                JSONObject jsonObj_rec = new JSONObject(data);
                String json_msg = jsonObj_rec.getString("json_msg");
                JSONObject jsonObj = new JSONObject(json_msg);

                id_receb = jsonObj.getInt("id");

                System.out.printf("(-> %d) Recebido de %d\n",meu_id,id_receb);

                if (id_receb != id_mestre) {
                    //Recebe tempo dos escravos

                    byte[] msg_signature = Base64.getDecoder().decode(jsonObj_rec.getString("signature"));
                    //byte[] msg_signature = jsonObj_rec.getString("signature").getBytes();

                    int id_slave = jsonObj.getInt("id");

                    // Pega chave publica do escravo
                    String pubkey_str = processos.get_pubKey(id_slave);
                    DSAPublicKey pubkey_slave = dsapack.DSA.Str2publicKey(pubkey_str);

                    boolean verif = dsapack.DSA.verify(pubkey_slave, json_msg.getBytes(), msg_signature);

                    if (verif) {
                        String msg = jsonObj.getString("msg");

                        if (msg.equals("Meu tempo")) {
                            System.out.printf("       msg info: %s, tempo: %d\n",msg,jsonObj.getLong("tempo"));
                            processos.salvaTempo(jsonObj.getLong("tempo"), id_slave);
                            processos.salva_trec(relogio.timePC(), id_slave);
                        }
                    } else {
                        System.out.printf("[!! %d] U fake bruh 1!\n", id_slave);
                    }
                } else {
                    // Slave recebe msg

                    // Abre mensagem
                    byte[] msg_signature = Base64.getDecoder().decode(jsonObj_rec.getString("signature"));

                    int id = jsonObj.getInt("id");

                    // Pega chave publica do suposto mestre
                    String pubkey_str = processos.get_pubKey(id);
                    DSAPublicKey pubkey_rec = dsapack.DSA.Str2publicKey(pubkey_str);

                    boolean verif = dsapack.DSA.verify(pubkey_rec, json_msg.getBytes(), msg_signature);

                    if (verif) {
                        String msg = jsonObj.getString("msg");

                        System.out.printf("[<> %d] mensagem verficada: %s, id: %d\n", meu_id,msg, id);

                        if (msg.equals("Ajuste")) {
                            long t = jsonObj.getLong("tempo");
                            System.out.printf("       msg info: %s Ajuste de %d ms\n",msg,t);
                            relogio.ajustaTempo(t);
                            processos.salvaAjuste(t, meu_id);
                        } else if (msg.equals("Quero tempo")) {
                            // Prepara json resposta

                            JSONObject jsonObj_send = new JSONObject();
                            jsonObj_send.put("msg", "Meu tempo");
                            jsonObj_send.put("id", id);
                            jsonObj_send.put("tempo", relogio.timePC());

                            messages.add(jsonObj_send);

                            //out.writeUTF(json_string);
                        }
                    } else {
                        System.out.printf("[!! %d] U fake bruh 2!\n", id);
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
