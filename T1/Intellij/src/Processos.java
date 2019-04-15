package multicastpackage;


import java.util.LinkedList;

//Lista com todos os procesos
public class Processos {

    LinkedList<Processo> processos;

    public Processos(){
        processos = new LinkedList<Processo>();
    }

    //adiciona um novo id na lista
    public void add (int id, String pkey, int porta_processo){
        Processo processo = new Processo(id, pkey, porta_processo);
        processos.add(processo);
    }


    //deletar um processo da lista
    public boolean delete (int id){
        Processo processo;
        int size = processos.size();
        //procura o processo
        for (int i=0;i<size;i++){
            processo = processos.get(i);
            if (processo.id == id){
                processos.remove(i);
                return true;
            }
        }
        return false;
    }

    public int procuraMestre(){
        Processo processo;
        int size = processos.size();
        //procura o menor id que sera o mestre
        int menor_id = 1000;
        for (int i=0;i<size;i++){
            processo = processos.get(i);
            if (processo.id<menor_id){
                menor_id = processo.id;
            }
        }
        return menor_id;
    }

    //salva o tempo de um processo especifico
    public void salvaTempo(long tempo, int id) {

        Processo processo;
        int size = processos.size();
        //procura o processo
        for (int i=0;i<size;i++){
            processo = processos.get(i);
            if (processo.id == id){
                processo.setTempo(tempo);
            }
        }
    }

    public void print(){
        Processo processo;
        int size = processos.size();
        //procura o processo
        for (int i=0;i<size;i++) {
            processo = processos.get(i);
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
    public void setTempo(long atime){atime = time; }
}