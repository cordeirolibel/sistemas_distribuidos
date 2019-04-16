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

import dsapack.DSA;

class unicastConnection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int meu_id;
    int id_mestre;
    Processos processos;
    Relogio relogio;

    public unicastConnection (Socket aClientSocket, Procesos aProcessos, Relogio arelogio){
        processos = aProcessos;
        relogio = arelogio;

        try {
            clientSocket = aClientSocket;
            in = new DataInputStream( clientSocket.getInputStream());
            out =new DataOutputStream( clientSocket.getOutputStream());
            this.start();
        } catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
    }

    public void run(){
        try {
            // Recebe resposta
            String data = in.readUTF();

            if (meu_id == id_mestre){
                //Recebe tempo dos escravos
                JSONObject jsonObj = new JSONObject(data);
                String msg = jsonObj.getString("msg");
                int id_slave = jsonObj.getInt("id");

                if (msg.equals("Meu tempo")){
                    byte[] signature = jsonObj.getString("signature").getBytes();

                    String pubkey_str = processos.get_pubKey(id_slave);
                    DSAPublicKey pubkey_slave = DSA.Str2publicKey(pubkey_str);

                    boolean verif = DSA.verify(pubkey_slave, msg.getBytes(), signature);

                    if (verif){
                        processos.salvaTempo(jsonObj.getLong("tempo"), id_slave);
                    }
                    else{
                        System.out.printf("[!! %d] I'm fake bruh!\n",meu_id);
                    }
                }
            }
            else{
                // Slave recebe ajuste
                JSONObject jsonObj = new JSONObject(data);
                String msg = jsonObj.getString("msg");
                int id = jsonObj.getInt("id");

                if (id != id_mestre){
                    if (msg.equals("Ajuste")){
                        byte[] signature = jsonObj.getString("signature").getBytes();

                        String pubkey_str = processos.get_pubKey(id);
                        DSAPublicKey pubkey_slave = DSA.Str2publicKey(pubkey_str);

                        boolean verif = DSA.verify(pubkey_slave, msg.getBytes(), signature);

                        if (verif){
                            long t = jsonObj.getLong("tempo");
                            relogio.ajustaTempo(t);
                            processos.salvaAjuste(t, meu_id);
                        }
                        else{
                            System.out.printf("[!! %d] I'm fake bruh!\n",meu_id);
                        }
                    }
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