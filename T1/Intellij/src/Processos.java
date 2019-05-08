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

    public long get_tempo(int id){
        Processo processo;
        int size = listaProcessos.size();

        for (int i=0;i<size;i++){
            processo = listaProcessos.get(i);
            if (processo.id == id){
                return processo.time;
            }
        }

        return 0;
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
                processo.setAjuste(ajuste);
            }
        }
    }

    //salva o tempo de um processo t0
    public void salva_tzero(long t, int i) {

        Processo processo;
        int size = listaProcessos.size();
        //procura o processo
        processo = listaProcessos.get(i);
        processo.set_tzero(t);
    }

    //salva o tempo de um processo t1
    public void salva_trec(long t, int id) {

        Processo processo;
        int size = listaProcessos.size();
        //procura o processo
        for (int i=0;i<size;i++){
            processo = listaProcessos.get(i);
            if (processo.id == id){
                processo.set_trec(t);
            }
        }
    }

    //salva o tempo de um processo t0
    public void salva_tzero_id(long t, int id) {
        Processo processo;
        int size = listaProcessos.size();
        //procura o processo
        for (int i=0;i<size;i++){
            processo = listaProcessos.get(i);
            if (processo.id == id){
                processo.set_tzero(t);
            }
        }
    }

    public long get_ajuste_interador(int interador){
        Processo processo;
        processo = listaProcessos.get(interador);
        return processo.ajuste;
    }

    public void print(){
        Processo processo;
        int size = listaProcessos.size();
        System.out.println("========== Tabela de Berkeley ==========");
        //procura o processo
        for (int i=0;i<size;i++) {
            processo = listaProcessos.get(i);
            System.out.printf(" id: %d\tporta: %d\ttime: %d\trtt: %d\testimado: %d\tajuste: %d\n",processo.id,processo.porta_unicast,processo.time,processo.rtt,processo.estimado,processo.ajuste);
        }
    }

    public int getPorta(int iterador) {
        try {
            return listaProcessos.get(iterador).porta_unicast;
        }
        catch (IndexOutOfBoundsException e){
            return 0;
        }
    }

    public int getPortaId(int id) {
        Processo processo;
        int size = listaProcessos.size();

        for (int i=0;i<size;i++){
            processo = listaProcessos.get(i);
            if (processo.id == id){
                return processo.porta_unicast;
            }
        }

        return 0;
    }

    public int size(){
        return listaProcessos.size();
    }

    public void Berkeley(int meu_id){
        Processo processo;
        int size = listaProcessos.size();
        long rtt, estimado, media = 0;
        int n_validos = 0;

        //calcula RTTs e Estimados
        long meu_relogio = get_tempo(meu_id);
        for (int i=0;i<size;i++){
            processo = listaProcessos.get(i);
            //rtt
            rtt = processo.t_rec - processo.t_zero;
            processo.setRTT(rtt);
            //estimado
            estimado = processo.time + rtt/2;
            processo.setEstimado(estimado);

            //RTT maximo de 10 ms
            if (rtt<=35){
                media += estimado;
                n_validos += 1;
            }
        }

        media = media/n_validos;
        System.out.printf("(## %d) media estimada: %d\n",meu_id,media);

        long ajuste;
        //ajuste
        for (int i=0;i<size;i++) {
            processo = listaProcessos.get(i);
            ajuste = media - processo.time;
            processo.setAjuste(ajuste);
        }
    }

}

//objeto para um processo
class Processo {
    int id;
    long time;
    long ajuste;
    long rtt;
    long estimado;
    String pubKey;
    int porta_unicast;
    long t_zero;
    long t_rec;

    public Processo(int aid, String pkey, int porta_processo)
    {
        id = aid;
        ajuste = 0;
        time = 0;
        rtt = 0;
        estimado = 0;
        pubKey = pkey;
        porta_unicast = porta_processo;
        t_zero = 0;
        t_rec = 0;
    }
    public void setTempo(long atime){time = atime;}
    public void setAjuste(long aajuste){ajuste = aajuste;}
    public void setRTT(long artt){rtt = artt;}
    public void setEstimado(long aestimado){estimado = aestimado;}
    public void set_tzero (long atzero) {t_zero = atzero;}
    public void set_trec (long atrec) {t_rec = atrec;}

}