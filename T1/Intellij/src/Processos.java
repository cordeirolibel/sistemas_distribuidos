package multicastpackage;


import java.util.LinkedList;

//Lista com todos os procesos
public class Processos {

    LinkedList<Processo> listaProcessos;

    public Processos(){
        listaProcessos = new LinkedList<Processo>();
    }

    //adiciona um novo id na lista
    public void add (int id, String pkey, int porta_processo){
        Processo processo = new Processo(id, pkey, porta_processo);
        listaProcessos.add(processo);
    }

    //deletar um processo da lista
    public boolean delete (int id){
        Processo processo;
        int size = listaProcessos.size();
        //procura o processo
        for (int i=0;i<size;i++){
            processo = listaProcessos.get(i);
            if (processo.id == id){
                listaProcessos.remove(i);
                return true;
            }
        }
        return false;
    }

    public int procuraMestre(){
        Processo processo;
        int size = listaProcessos.size();
        //procura o menor id que sera o mestre
        int menor_id = 1000;
        for (int i=0;i<size;i++){
            processo = listaProcessos.get(i);
            if (processo.id<menor_id){
                menor_id = processo.id;
            }
        }
        return menor_id;
    }

    public String get_pubKey(int id){
        Processo processo;
        int size = listaProcessos.size();

        for (int i=0;i<size;i++){
            processo = listaProcessos.get(i);
            if (processo.id == id){
                return processo.pubKey;
            }
        }

        return null;
    }

    //salva o tempo de um processo especifico
    public void salvaTempo(long tempo, int id) {

        Processo processo;
        int size = listaProcessos.size();
        //procura o processo
        for (int i=0;i<size;i++){
            processo = listaProcessos.get(i);
            if (processo.id == id){
                processo.setTempo(tempo);
            }
        }
    }

    //salva o tempo de um processo especifico
    public void salvaAjuste(long ajuste, int id) {

        Processo processo;
        int size = listaProcessos.size();
        //procura o processo
        for (int i=0;i<size;i++){
            processo = listaProcessos.get(i);
            if (processo.id == id){
                processo.setTempo(ajuste);
            }
        }
    }

    public void print(){
        Processo processo;
        int size = listaProcessos.size();
        //procura o processo
        for (int i=0;i<size;i++) {
            processo = listaProcessos.get(i);
            System.out.printf("  %d\n",processo.id);
        }
    }

}

//objeto para um processo
class Processo {
    int id;
    long time;
    long ajuste;
    String pubKey;
    int porta_unicast;

    public Processo(int aid, String pkey, int porta_processo)
    {
        id = aid;
        ajuste = 0;
        time = 0;
        pubKey = pkey;
        porta_unicast = porta_processo;
    }
    public void setTempo(long atime){time = atime;}
    public void setAjuste(long aajuste){ajuste = aajuste;}
}