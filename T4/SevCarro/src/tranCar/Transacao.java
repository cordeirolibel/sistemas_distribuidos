package tranCar;

import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Transacao implements Serializable {

    static int n_tran = 0;
    int id_tran;
    int id_recurso;
    int id_clie;
    String voto_banco;
    String voto_coordenador;
    String voto_cliente;
    String status;
    Logger logger;
    FileHandler fh_logger;

    public Transacao(){
        n_tran += 1;
        id_tran = n_tran;
        id_clie = -1;
        id_recurso = -1;
        status = "provisoria";

        voto_banco = "" ;
        voto_coordenador = "";
        voto_cliente = "";

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
        id_tran = n_tran;
        id_clie = -1;
        id_recurso = -1;
        status = "provisoria";

        voto_banco = "" ;
        voto_coordenador = "";
        voto_cliente = "";

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
                    n_tran = Math.max(id_tran,n_tran);
                }
                else if (words[1].equals("idCliente")){
                    id_clie = Integer.parseInt(words[2]);
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

                // 3 Votos
                else if (words[1].equals("banco")){
                    voto_banco = words[2];
                }
                else if (words[1].equals("coordenador")){
                    voto_coordenador = words[2];
                }
                else if (words[1].equals("cliente")) {
                    voto_cliente = words[2];
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
        System.out.printf("id:%d n_tran:%d\n",id_tran,n_tran);
        System.out.printf("id clie:%d id recurso:%d\n",id_clie,id_recurso);
        System.out.printf("banco: %s \n",voto_banco);
        System.out.printf("coordenado: %s \n",voto_coordenador);
        System.out.printf("cliente: %s \n",voto_cliente);
        System.out.printf("status: %s\n",getStatus());
    }


    //quem = "banco","coordenador" ou "cliente"
    //voto = "sim","nao" ou ""
    //voto == "" siginifica voto em aberto, nao decido ainda
    public void setVoto(String quem,String voto){
        if(quem.equals("banco")){
            voto_banco = voto;
            logger.info("banco".concat(" ").concat(voto));
        }
        else if(quem.equals("coordenador")){
            voto_coordenador = voto;
            logger.info("coordenador".concat(" ").concat(voto));
        }
        else if(quem.equals("cliente")){
            voto_cliente = voto;
            logger.info("cliente".concat(" ").concat(voto));
        }
    }

    //quem = "banco","coordenador" ou "cliente"
    //voto = "sim","nao" ou ""
    //voto == "" siginifica voto em aberto, nao decido ainda
    public String getVoto(String quem){
        if(quem.equals("banco")){
            return voto_banco;
        }
        else if(quem.equals("coordenador")){
            return voto_coordenador;
        }
        else if(quem.equals("cliente")){
            return voto_cliente;
        }
        return "";
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

    public int getId_clie() {
        return id_clie;
    }

    public void setId_clie(int id_clie) {
        this.id_clie = id_clie;
        logger.info("idCliente".concat(" ").concat(String.valueOf(id_clie)));
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

    public void efetiva(){
        setStatus("efetivada");
    }


}
