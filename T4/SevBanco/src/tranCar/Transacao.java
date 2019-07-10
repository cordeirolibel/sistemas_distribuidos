package tranCar;

import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Transacao implements Serializable {

    int id_tran;
    int id_recurso;
    float valor;
    String status;
    Logger logger;
    FileHandler fh_logger;

    public Transacao(int id_tran, float valor){
        this.valor  = valor;
        this.id_tran = id_tran;
        id_recurso = -1;
        status = "provisoria";


        //--------------------------
        //Cria log
        String file_name = "logs/log_".concat(String.valueOf(id_tran)).concat(".log");

        logger = Logger.getLogger(file_name);

        try {

            // This block configure the logger with handler and formatter
            fh_logger = new FileHandler(file_name);
            logger.addHandler(fh_logger);
            SimpleFormatter formatter = new SimpleFormatter();
            fh_logger.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //primeiro log
        logger.info("id ".concat(String.valueOf(id_tran)));
    }

    //carrega a transacao pelo log
    public Transacao(String log_path){
        id_tran = -1;
        id_recurso = -1;
        status = "provisoria";

        //--------------------------
        // Lendo o log
        try (BufferedReader br = new BufferedReader(new FileReader(log_path))) {
            String line;
            while ((line = br.readLine()) != null) {

                //verifica cada linha e atualiza a transacao
                String words [] = line.split(" ");

                // 1 Ids
                if (words[1].equals("id")){
                    id_tran = Integer.parseInt(words[2]);
                }
                else if (words[1].equals("recurso")){
                    id_recurso = Integer.parseInt(words[2]);
                }

                // 2 Transacao
                else if (words[1].equals("cancelada")){
                    status = "cancelada";
                }
                else if (words[1].equals("efetivada")){
                    status = "efetivada";
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //criando o logger
        logger = Logger.getLogger(log_path);
        try {

            // This block configure the logger with handler and formatter
            fh_logger = new FileHandler(log_path,true);
            logger.addHandler(fh_logger);
            SimpleFormatter formatter = new SimpleFormatter();
            fh_logger.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void print(){
        System.out.printf("-------------print tran-----------------\n");
        System.out.printf("id:%d \n",id_tran);
        System.out.printf("id recurso:%d\n",id_recurso);
        System.out.printf("status: %s\n",getStatus());
    }


    public float getValor() {
        return valor;
    }

    public int getId_tran() {
        return id_tran;
    }

    public int getId_recurso() {
        return id_recurso;
    }

    public void setId_recurso(int id){
        id_recurso = id;
        logger.info("recurso".concat(" ").concat(String.valueOf(id_recurso)));
    }

    //efetivada, cancelada ou provisoria.
    public String getStatus(){
        return status;
    }

    //efetivada, cancelada ou provisoria.
    public void setStatus(String new_status){
        status = new_status;
        logger.info(new_status);
    }

    public void aborta(){
        setStatus("cancelada");
    }

    public boolean estaAbortada(){
        if (status.equals("cancelada")){
            return true;
        }
        return false;
    }

    public void efetiva(){
        setStatus("efetivada");
    }


}
