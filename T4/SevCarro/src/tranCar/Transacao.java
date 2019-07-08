package tranCar;

import java.io.Serializable;

public class Transacao implements Serializable {

    static int n_tran = 1;
    int id_tran;
    int id_recurso;
    InterfaceCli clieImpl;

    String status;

    public Transacao(){
        id_tran = n_tran;
        n_tran += 1;
        status = "provisoria";
    }

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
    }


}
