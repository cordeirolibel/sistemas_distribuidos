package multicastpackage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Relogio {

    long ajusteTempo;
    SimpleDateFormat sdf;
    LinkedList<Processo> processos;

    public Relogio(LinkedList<Processo> aprocessos){
        // args give message contents and destination multicast group (e.g. "228.5.6.7")
        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        ajusteTempo = 0;
        aprocessos = processos;
    }

    // adiciona um ajuste ao relogio
    public long ajustaTempo(long ajuste) {
        ajusteTempo = ajusteTempo + ajuste;
        return ajusteTempo;
    }

    //retorna o time do PC em formato string
    //  "2019/04/03 15:06:47.954"
    public String timePCstring(){
        return sdf.format(new Date(timePC()));
    }

    //retorna o time do PC em formato long
    public long timePC(){
        //time do pc em fomato long
        Date now = new Date();
        long nowL = now.getTime();

        //add ajuste
        long timeProcessoL = nowL + ajusteTempo;
        Date timeProcesso =  new Date(timeProcessoL);
        return timeProcessoL;
    }

    public void Berkeley(){
        Processo processo;
        int size = processos.size();
        //for (int i=0;i<size;i++){
        //    processo = processos.get(i);
        //   if (processo.id == id){
        //        processo.setTempo(tempo);
        //    }
        //}

    }
}
