package tranCar;

import java.io.*;

public class Transacao implements Serializable {

    static int n_tran = 1;
    int id_tran;
    int id_recurso;
    InterfaceCli clieImpl;
    String voto_banco;
    String voto_coordenador;
    String voto_cliente;
    String status;

    public Transacao(){
        id_tran = n_tran;
        n_tran += 1;
        status = "provisoria";

        voto_banco = "" ;
        voto_coordenador = "";
        voto_cliente = "";
        save();
    }

    //quem = "banco","coordenador" ou "cliente"
    //voto = "sim","nao" ou ""
    //voto == "" siginifica voto em aberto, nao decido ainda
    public void setVoto(String quem,String voto){
        if(quem.equals("banco")){
            voto_banco = voto;
        }
        else if(quem.equals("coordenador")){
            voto_coordenador = voto;
        }
        else if(quem.equals("cliente")){
            voto_cliente = voto;
        }
        save();
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


    //salva a transacao
    public void save() {

        String file_name = "logs/log_".concat(String.valueOf(id_tran)).concat(".txt");

        try {
            FileOutputStream f = new FileOutputStream(new File(file_name));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(this); //TODO: fazer diferente

            //close
            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //carrega listas de um arquivo local
    /*private void loadfile() throws FileNotFoundException, IOException, ClassNotFoundException{
        FileInputStream fi = new FileInputStream(new File("myObjects.txt"));
        ObjectInputStream oi = new ObjectInputStream(fi);

        // Read objects
        lista_oferta = (LinkedList<Oferta>) oi.readObject();
        lista_horarios_mot = (LinkedList<Horarios>) oi.readObject();

        //close
        oi.close();
        fi.close();
    }*/

    public int getId_tran() {
        return id_tran;
    }

    public int getId_recurso() {
        return id_recurso;
    }

    public String getStatus(){
        return status;
    }

    public void setId_recurso(int id){
        id_recurso = id;
    }

    public void setInterfaceCli( InterfaceCli interface_cli){
        clieImpl = interface_cli;
    }
    public InterfaceCli getInterfaceCli(){
        return clieImpl;
    }

    public void aborta(){
        status = "cancelada";
        save();
    }


}
