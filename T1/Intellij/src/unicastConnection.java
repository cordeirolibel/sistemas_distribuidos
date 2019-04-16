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
import java.util.Queue;

import dsapack.DSA;

class unicastConnection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int meu_id;
    int id_mestre;
    Processos processos;
    Relogio relogio;
    Queue <JSONObject> messages;

    public unicastConnection (Socket aClientSocket, Processos aProcessos, Relogio arelogio, Queue <JSONObject> amessages){
        processos = aProcessos;
        relogio = arelogio;
        messages = amessages;

        try {
            clientSocket = aClientSocket;
            in = new DataInputStream( clientSocket.getInputStream());
            out = new DataOutputStream( clientSocket.getOutputStream());
            this.start();
        } catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
    }

    public void run(){
        try {
            // Recebe resposta
            String data = in.readUTF();

            if (meu_id == id_mestre){
                //Recebe tempo dos escravos
                JSONObject jsonObj_rec = new JSONObject(data);

                // Abre mensagem
                String json_msg = jsonObj_rec.getString("json_msg");
                byte[] msg_signature = jsonObj_rec.getString("signature").getBytes();

                JSONObject jsonObj = new JSONObject(json_msg);
                int id_slave = jsonObj.getInt("id");

                // Pega chave publica do escravo
                String pubkey_str = processos.get_pubKey(id_slave);
                DSAPublicKey pubkey_slave = DSA.Str2publicKey(pubkey_str);

                boolean verif = DSA.verify(pubkey_slave, json_msg.getBytes(), msg_signature);

                if (verif){
                    String msg = jsonObj.getString("msg");
                    if (msg.equals("Meu tempo")){
                        processos.salvaTempo(jsonObj.getLong("tempo"), id_slave);
                    }
                }
                else{
                    System.out.printf("[!! %d] U fake bruh!\n", id_slave);
                }
            }
            else{
                // Slave recebe msg
                JSONObject jsonObj_rec = new JSONObject(data);

                // Abre mensagem
                String json_msg = jsonObj_rec.getString("json_msg");
                byte[] msg_signature = jsonObj_rec.getString("signature").getBytes();

                JSONObject jsonObj = new JSONObject(json_msg);
                int id = jsonObj.getInt("id");

                // Pega chave publica do suposto mestre
                String pubkey_str = processos.get_pubKey(id);
                DSAPublicKey pubkey_slave = DSA.Str2publicKey(pubkey_str);

                boolean verif = DSA.verify(pubkey_slave, json_msg.getBytes(), msg_signature);

                if (verif){
                    String msg = jsonObj.getString("msg");

                    if (msg.equals("Ajuste")){
                        long t = jsonObj.getLong("tempo");
                        relogio.ajustaTempo(t);
                        processos.salvaAjuste(t, meu_id);
                    }
                    else if (msg.equals("Quero tempo")){
                        // Prepara json resposta

                        JSONObject jsonObj_send = new JSONObject();
                        jsonObj_send.put("msg", "Meu tempo");
                        jsonObj_send.put("id", meu_id);
                        jsonObj_send.put("tempo", relogio.timePC());

                        messages.add(jsonObj_send);

                        //out.writeUTF(json_string);
                    }
                }
                else{
                    System.out.printf("[!! %d] U fake bruh!\n", id);
                }
            }

        }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
        } catch(IOException e) {System.out.println("readline:"+e.getMessage());
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
        } finally{ try {clientSocket.close();}catch (IOException e){/*close failed*/}}

    }


}